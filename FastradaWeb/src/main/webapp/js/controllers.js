function HomeController($scope, SessionData) {
    $scope.sessions = [];

    $scope.hasSessions = function () {
        if ($scope.sessions.length > 0)
            return true;

        return false;
    }

    SessionData.getSessions().success(function (d) {
        $scope.sessions = d;
    });
}

function SessionDetailController($scope, $routeParams, SessionData,$q) {
    $scope.sliderValues = [0, 500];

    var parameter;

    SessionData.getSessionParameters($routeParams.sessionId).success(function (d) {
        $scope.parameters = d;

        if($routeParams.parameterName==null){
            parameter = $scope.parameters[0];
        }
        else {
            parameter = $routeParams.parameterName;
        }

        getData();
    });

    $scope.chartTypes = ['LineChart', 'AreaChart', 'ColumnChart', 'BarChart', 'Table']; //'PieChart',
    $scope.visibleGraph = false;

    // chart variables
    var rawD1;
    var rawD2;
    var result = [];
    var resultRows = [];
    var minSeconds = 0;
    var maxSeconds = 0;

    $scope.sliderMaxValue = 100;

    // move test data to tests
    var rawJson = [
        {    'timestamp': '1393405044000',
            'value': '100'
        },
        {    'timestamp': '1393405072000',
            'value': '110'
        },
        {    'timestamp': '1393405076000',
            'value': '120'
        },
        {    'timestamp': '1393405080000',
            'value': '123'
        },
        {    'timestamp': '1393405086000',
            'value': '99'
        }
    ];

    $scope.sessionId = $routeParams.sessionId;

    $scope.isActiveParameter = function (name) {
        if (name == parameter)
            return true;

        return false;
    };

    $scope.chart = {
        "type": 'AreaChart',
        "cssStyle": "height:400px; width:100%;border: 1px #ccc solid",
        "data": [],
        "options": {
            "pointSize": 0,
            "title": "Temperature",
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
        "displayed": true
       // "view": {columns: [0,1,2]}
    }
    ;

    //herschreven voor slider
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

    $scope.createGraphData = function (rawJSON,parameterNames) {
        result = {
            "cols": [
                {
                    "id": "seconds",
                    "label": "Seconds",
                    "type": "number",
                    "p": {}
                }
            ],
            "rows": [
            ]
        };

        var firstTime = 0;
        var formattedTime = 0;

        resultRows = [];

        var asCounter = 0;

        var axisColors = ['#3366cc','#dc3912']

        $scope.chart.options.vAxes={};

        angular.forEach(rawJSON, function (rawDataSet) {
            var maxYValue = 0;
            var minYValue = 0;

            var maxDataSetLength = 0;

            for(var index = 0;index<rawJSON.length;index++){
                if(maxDataSetLength<rawJSON[index].length){
                    maxDataSetLength=rawJSON[index].length;
                }
            }

            for (var parameterRecord = 0; parameterRecord < rawDataSet.length; parameterRecord++) {

                console.log(rawDataSet);

                if(maxYValue <rawDataSet[parameterRecord].value){
                    maxYValue = rawDataSet[parameterRecord].value;
                }

                if(minYValue>rawDataSet[parameterRecord].value){
                    minYValue=rawDataSet[parameterRecord].value;
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

                if(parameterRecord >=rawDataSet.length-1){

                }

                if (parameterRecord == rawDataSet.length - 1) {
                    if(maxSeconds < formattedTime){
                    maxSeconds = formattedTime;
                    }
                }
            }

            result.cols.push(
                {
                    "id": parameterNames[asCounter],
                    "label": parameterNames[asCounter],
                    "type": "number",
                    "p": {}
                });

            result.rows = resultRows;
            $scope.sliderValues = [0, maxSeconds];
            $scope.sliderMaxValue = maxSeconds;

            $scope.chart.options.vAxes[asCounter] = {
//                minValue: minYValue,
//                    maxValue: maxYValue,
                label: parameterNames[asCounter],
                textColor: axisColors[asCounter]

            };

            asCounter++;
        });


        $scope.chart.data = result;
        $scope.chart.options.title = parameter;
        $scope.visibleGraph = true;
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
        $scope.createGraphData([newData, newData2]);
    };

//    var getData = function () {
//        SessionData.getSessionParameter($scope.sessionId, parameter)
//            .then(function (raw1) {
//                SessionData.getSessionParameter($scope.sessionId, 'gear').then(
//                    function (raw2) {
//                        $scope.createGraphData([raw1.data, raw2.data],[parameter,'gear']);
//                    });
//            });
//    };

//    var getData = function () {
//        SessionData.getSessionParameter($scope.sessionId, parameter)
//            .then(function (raw1) {
//                $scope.createGraphData([raw1.data],[parameter]);
//            });
//    };
//
//    var getData = function () {
//    var promiselijst = [];
//
//    for(var index = 0;index<$scope.parameters.length;index++){
//        promiselijst[index]= SessionData.getSessionParameter($scope.sessionId, $scope.parameters[index])
//    }
//
//    $q.all(promiselijst).then(function (results) {
//        var graphRawJsonData = [];
//
//        angular.forEach(results, function (result) {
//        graphRawJsonData.push(result.data);
//        });
//
//            $scope.createGraphData(graphRawJsonData,$scope.parameters);
//    });
//    }


    var getData = function () {
        var rawDataLijst = [];
        var counter = 0;

        var getParameterValue = function(parameterName){
            SessionData.getSessionParameter($scope.sessionId,parameterName).then(function (raw){
            rawDataLijst[counter]=raw.data;
                counter++;
                if($scope.parameters[counter]!=null){
                    getParameterValue($scope.parameters[counter]);
                }
                else{
                    $scope.createGraphData(rawDataLijst,$scope.parameters);
                }
            })};

        getParameterValue($scope.parameters[counter]);
    }

}