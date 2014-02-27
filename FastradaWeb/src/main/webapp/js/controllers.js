function HomeController($scope) {

    $scope.sessions = [
        {
            'sessionId': '0',
            'name': 'PJ',
            'startTime': '13:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '1',
            'name': 'Philip',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '2',
            'name': 'Thomas',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '3',
            'name': 'Peter',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '4',
            'name': 'Carlo',
            'startTime': '14:00',
            'date': '21-2-2014'
        } ,
        {
            'sessionId': '5',
            'name': 'Jonathan',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '0',
            'name': 'PJ',
            'startTime': '13:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '1',
            'name': 'Philip',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '2',
            'name': 'Thomas',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '3',
            'name': 'Peter',
            'startTime': '14:00',
            'date': '21-2-2014'
        },
        {
            'sessionId': '4',
            'name': 'Carlo',
            'startTime': '14:00',
            'date': '21-2-2014'
        } ,
        {
            'sessionId': '5',
            'name': 'Jonathan',
            'startTime': '14:00',
            'date': '21-2-2014'
        }
    ];

    $scope.hasSessions = function () {
        if ($scope.sessions.length > 0)
            return true;

        return false;
    }
}

function SessionDetailController($scope, $routeParams) {
    $scope.parameters = ['Temperature', 'Speed', 'FuelMap', 'RPM', 'Gear'];

    $scope.chartTypes = ['LineChart', 'AreaChart', 'ColumnChart', 'BarChart', 'Table']; //'PieChart',

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
            "pointSize": 5,
            "title": "Temperature",
            "isStacked": "true",
            "fill": 20,
            "displayExactValues": true,
            "vAxis": {
                "title": "Temperature in °C",
                "gridlines": {
                    "count": 10
                }
            },
            "hAxis": {
                "title": "Seconds"
            }
        },
        "displayed": true
    }
    ;

    $scope.setChartType = function (index) {
        $scope.chart.type = $scope.chartTypes[index];
    };

    $scope.isActiveChartType = function (index) {
        if ($scope.chartTypes[index] == $scope.chart.type)
            return true;

        return false;
    };

    $scope.createGraphData = function (rawJSON) {
        var result = {
            "cols": [
                {
                    "id": "seconds",
                    "label": "Seconds",
                    "type": "number",
                    "p": {}
                },
                {
                    "id": "Car",
                    "label": parameter,
                    "type": "number",
                    "p": {}
                }
            ],
            "rows": [
            ]
        };
        var firstTime = 0;
        var formattedTime = 0;

        for (var parameterRecord = 0; parameterRecord < rawJSON.length; parameterRecord++) {
            if (firstTime == 0) {
                firstTime = rawJSON[parameterRecord].timestamp;
            } else {
                formattedTime = rawJSON[parameterRecord].timestamp - firstTime;
                formattedTime = formattedTime / 1000;
            }

            result.rows.push({
                    'c': [
                        {"v": formattedTime },
                        {"v": rawJSON[parameterRecord].value}
                    ]
                }
            )
        }

        $scope.chart.data = result;
        $scope.chart.options.title = parameter;
    };

    $scope.createGraphData(rawJson);
}