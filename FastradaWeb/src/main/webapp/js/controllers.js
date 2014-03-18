function MetaDataController($scope, SessionData,SessionIdFactory){
    $scope.showMetaData = false;

    $scope.$on("sessionIdChanged",function(event,args) {
    if(SessionIdFactory.get()!=null){
    SessionData.getSessionMetaData(SessionIdFactory.get()).success(function (metaData){
        $scope.session = metaData;
        $scope.showMetaData = true;
    })
    }
        else {
        $scope.session = null;
        $scope.showMetaData = false;
    }
    });
}

function HomeController($scope, SessionData, SessionIdFactory, $rootScope) {
    $scope.sessions = [];

    SessionIdFactory.set(null);
    $rootScope.$broadcast('sessionIdChanged');

    $scope.hasSessions = function () {
        if ($scope.sessions.length > 0)
            return true;

        return false;
    };

    var getData = function () {
        SessionData.getSessions().success(function (d) {
            $scope.sessions = d;
            $scope.visuals = [];
            angular.forEach(d, function (allsessions) {
                $scope.visuals.push(false);
            });
        });
    };
    getData();

    $scope.showMessage = function (value) {
        if ($scope.visuals.length >= value) {
            return $scope.visuals[value];
        }
        return false;
    };

    $scope.setVisual = function (value) {
        if ($scope.visuals.length >= value) {
            $scope.visuals[value] = !$scope.visuals[value];
        }
    };

    $scope.delete = function (id) {
        SessionData.deleteSession(id).then(function () {
            toastr.success("Successfully deleted session " + id);
            getData();
        });

    };
}

function SessionDetailController($scope, $routeParams, SessionData,SessionIdFactory, $rootScope,$q) {
    var parameter;

    SessionIdFactory.set($routeParams.sessionId);
    $rootScope.$broadcast('sessionIdChanged');

    $scope.graphLoaded = false;
    $scope.showWarning = false;


    SessionData.getSessionParameters($routeParams.sessionId).success(function (d) {
        $scope.parameters = d;

        getData();
    });

    $scope.chartTypes = ['LineChart', 'AreaChart', 'ColumnChart', 'BarChart', 'Table']; //'PieChart',


    // chart variables
    var result = [];
    var resultRows = [];
    var maxSeconds = 0;

    $scope.sessionId = $routeParams.sessionId;

    $scope.isActiveParameter = function (parameterIndex) {
        var index = $scope.chart.view.columns.indexOf(parameterIndex + 1);
        if (index != -1)
            return true;

        return false;
    };

    $scope.chart = {
        "type": 'AreaChart',
        "cssStyle": "height:400px; width:100%;",
        "data": [],
        "options": {
            "legend": {
                position: 'top'
            },
            "pointSize": 0,
            "title": "",
            "isStacked": "true",
            "fill": 20,
            "displayExactValues": true,
            "series": {
                "0": {
                    targetAxisIndex: 0
                },
                "1": {
                    targetAxisIndex: 1
                }
            },
            "vAxes": {
                "0": {
                    minValue: 0,
                    maxValue: 130,
                    label: 'temperature in °C'
                },
                "1": {
                    minValue: 0,
                    maxValue: 260,
                    label: 'speed in kmh',
                    textColor: "#ff0000"
                }
            },
            "hAxis": {
                "title": "Seconds"
            }
        },
        "displayed": true,
        "view": {
            columns: [0, 1]
        }
    }
    ;

    $scope.updateGraph = function (sliderValues) {
        $scope.chart.data.rows = [];

        for (var count = 0; count < resultRows.length; count++) {
            if (resultRows[count].c[0].v >= sliderValues[0] && resultRows[count].c[0].v <= sliderValues[1]) {
                $scope.chart.data.rows.push(resultRows[count]);
            }
        }
    };

    $scope.setChartType = function (index) {
        $scope.chart.type = $scope.chartTypes[index];
    };

    $scope.isActiveChartType = function (index) {
        if ($scope.chartTypes[index] == $scope.chart.type)
            return true;

        return false;
    };

    $scope.createGraphData = function (rawJSON, parameterNames) {
        result = {
            "cols": [
                {
                    "id": "seconds",
                    "label": "Seconds",
                    "type": "number"
                }
            ],
            "rows": [
            ]
        };

        var firstTime = 0;
        var formattedTime = 0;

        resultRows = [];
        var asCounter = 0;
        var axisColors = ['#3366cc', '#dc3912']

        $scope.chart.options.vAxes = {};

        var maxDataSetLength = 0;

        for (var index = 0; index < rawJSON.length; index++) {
            if (maxDataSetLength < rawJSON[index].length) {
                maxDataSetLength = rawJSON[index].length;
            }
        }

        angular.forEach(rawJSON, function (rawDataSet) {
            var maxYValue = 0;
            var minYValue = 0;


            for (var parameterRecord = 0; parameterRecord < maxDataSetLength; parameterRecord++) {

                if (parameterRecord >= rawDataSet.length) {
                    if (resultRows[parameterRecord] != null) {
                        resultRows[parameterRecord].c.push({"v": null});
                    } else {
                        resultRows.push({
                                'c': [
                                    {"v": formattedTime },
                                    {"v": null}
                                ]
                            }
                        );
                    }
                } else {
                    if (maxYValue < rawDataSet[parameterRecord].value) {
                        maxYValue = rawDataSet[parameterRecord].value;
                    }

                    if (minYValue > rawDataSet[parameterRecord].value) {
                        minYValue = rawDataSet[parameterRecord].value;
                    }

                    if (firstTime == 0) {
                        firstTime = rawDataSet[parameterRecord].timestamp;
                    } else {
                        formattedTime = rawDataSet[parameterRecord].timestamp - firstTime;
                        formattedTime = formattedTime / 1000;
                    }

                    if (resultRows[parameterRecord] != null) {
                        resultRows[parameterRecord].c.push({"v": rawDataSet[parameterRecord].value});
                    } else {
                        resultRows.push({
                                'c': [
                                    {"v": formattedTime },
                                    {"v": rawDataSet[parameterRecord].value}
                                ]
                            }
                        );
                    }
                }


                if (parameterRecord == rawDataSet.length - 1) {
                    if (maxSeconds < formattedTime) {
                        maxSeconds = formattedTime;
                    }
                }
            }

            result.cols.push(
                {
                    "id": parameterNames[asCounter],
                    "label": parameterNames[asCounter],
                    "type": "number"
                });

            result.rows = resultRows;


            $scope.chart.options.vAxes[asCounter] = {
                label: parameterNames[asCounter],
                textColor: axisColors[asCounter]

            };

            asCounter++;
        });

        $scope.sliderValues = [0, maxSeconds];
        $scope.sliderMaxValue = maxSeconds;

        $scope.chart.data = result;
        $scope.chart.options.title = parameter;
        $scope.graphLoaded = true;
    };

    $scope.random500Data = function () {
        var newData = [];
        var newData2 = [];
        for (var extraDataIndex = 0; extraDataIndex < 500; extraDataIndex++) {
            var random = Math.floor((Math.random() * 20) + 1);
            newData.push({'timestamp': extraDataIndex * 1000, 'value': 100 + random});
            random = Math.floor((Math.random() * 20) + 1);
            newData2.push({'timestamp': extraDataIndex * 1000, 'value': 50 + random});
        }
        $scope.createGraphData([newData, newData2], ["TestData", "TestData2"]);
    };

    var getData = function () {
        var rawDataLijst = [];
       // var counter = 0;

        // recursief door alle parameters
//        var getParameterValue = function (parameterName) {
//            SessionData.getSessionParameter($scope.sessionId, parameterName)
//                .then(function (raw) {
//                    rawDataLijst[counter] = raw.data;
//                    counter++;
//                    if ($scope.parameters[counter] != null) {
//                        getParameterValue($scope.parameters[counter]);
//                    }
//                    else {
//                        $scope.createGraphData(rawDataLijst, $scope.parameters);
//                    }
//                })
//        };

//        var getParameterValue = function (parameterName, rawDataIndex) {
//           var promise = SessionData.getSessionParameter($scope.sessionId, parameterName)
//                .success(function (raw) {
//                    rawDataLijst[rawDataIndex] = raw.data;
//                })
//            return promise;
//        };

        var promises = [];

        for(var parameterIndex = 0; parameterIndex<$scope.parameters.length;parameterIndex++){
           promises[parameterIndex] = SessionData.getSessionParameter($scope.sessionId, $scope.parameters[parameterIndex]);
        }

        $q.all(promises).then(function (result){

            for(var resultIndex = 0; resultIndex<result.length;resultIndex++){
                rawDataLijst[resultIndex]= result[resultIndex].data;
            }

            $scope.createGraphData(rawDataLijst, $scope.parameters);
        })

        //getParameterValue($scope.parameters[counter]);
    };

    $scope.selectParameter = function (parameterIndex) {
        var index = $scope.chart.view.columns.indexOf(parameterIndex + 1);
        var maxParametersShown = 2;

        if (index != -1) { // parameter al in de lijst
            if ($scope.chart.view.columns.length == maxParametersShown + 1) // max aantal parameters
                $scope.chart.view.columns.splice(index, 1);
            else
                toastr.warning("The graph requires at least one parameter.")
        } else {
            if ($scope.chart.view.columns.length < maxParametersShown + 1) {
                $scope.chart.view.columns.push(parameterIndex + 1);
            } else {
                toastr.warning("You can only show 2 parameters at the same time.")
            }
        }
    };

}

function InfoController($scope, $routeParams, SessionData) {

    $scope.mapOptions = {
        center: new google.maps.LatLng(51.061946, 4.511737),
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    $scope.sessionId = $routeParams.sessionId;

    SessionData.getGpsData($scope.sessionId).success(function (data) {
        $scope.poly = [];
        angular.forEach(data, function (oneLine) {
            $scope.poly.push(new google.maps.LatLng(oneLine.coordinate.latitude, oneLine.coordinate.longitude));
        });
        if($scope.poly.Length>0){
            $scope.mapOptions.center = $scope.poly[0];
        }
    });

//    var polylineCoords = [new google.maps.LatLng(51.761946, 4.511737), new google.maps.LatLng(51.217408, 4.416611)];

    $scope.onMapIdle = function () {
        $scope.polyline = new google.maps.Polyline({
            path: $scope.poly,
            strokeColor: "#0000FF",
            strokeOpacity: 0.8,
            strokeWeight: 2,
            map: $scope.myMap
        });

    }

        SessionData.getSessionMetaData($scope.sessionId).success(function (metaData) {
                $scope.session = metaData;
        });
}