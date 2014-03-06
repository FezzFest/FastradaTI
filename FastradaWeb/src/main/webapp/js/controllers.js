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

function SessionDetailController($scope, $routeParams, SessionData) {
    $scope.sliderValues = [0, 500];

    $scope.parameters = ['Temperature', 'Speed', 'FuelMap', 'RPM', 'Gear'];

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

    var parameter = "Temperature";

    $scope.chartMinMax = {"min": 0, "max": 500};


    if ($routeParams.parameterName != null) {
        parameter = $routeParams.parameterName;
    }

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

    $scope.createGraphData = function (rawJSON) {
        result = {
            "cols": [
                {
                    "id": "seconds",
                    "label": "Seconds",
                    "type": "number",
                    "p": {}
                },
                {
                    "id": "temperature",
                    "label": "temperature",
                    "type": "number",
                    "p": {}
                },
                {
                    "id": "speed",
                    "label": "speed",
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

        angular.forEach(rawJSON, function (rawDataSet) {
            for (var parameterRecord = 0; parameterRecord < rawDataSet.length; parameterRecord++) {
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

                if (parameterRecord == rawDataSet.length - 1) {
                    maxSeconds = formattedTime;
                }
            }

            result.rows = resultRows;
            $scope.sliderValues = [0, maxSeconds];
            $scope.sliderMaxValue = maxSeconds;
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

    var getData = function () {
        SessionData.getSessionParameter($scope.sessionId, 'speed')
            .then(function (raw1) {
                SessionData.getSessionParameter($scope.sessionId, 'gear').then(
                    function (raw2) {
                        $scope.createGraphData([raw1.data, raw2.data]);
                    });
            });
    };

    getData();

}