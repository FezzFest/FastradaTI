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

    $scope.hasSessions = function(){
        if($scope.sessions.length>0)
            return true;

        return false;
    }
}

function SessionDetailController($scope,$routeParams) {
    $scope.parameters = ['Temperature','Speed','FuelMap','RPM','Gear'];

    $scope.sessionId = $routeParams.sessionId;

    var parameter = "Temperature";

    if($routeParams.parameterName != null){
        parameter = $routeParams.parameterName;
    }

    $scope.isActiveParameter = function(name){
        if(name == parameter)
            return true;

        return false;
    }

    var data = {
        "cols": [
            {
                "id": "month",
                "label": "Month",
                "type": "string",
                "p": {}
            },
            {
                "id": "Car",
                "label": "Engine Temperature",
                "type": "number",
                "p": {}
            }
        ],
        "rows": [
            {
                "c": [
                    {
                        "v": "0"
                    },
                    {
                        "v": 0,
                        "f": "Label for a temperature"
                    }
                ]
            },
            {
                "c": [
                    {
                        "v": "1"
                    },{
                        "v": 10
                    }
                ]
            },
            {
                "c": [
                    {
                        "v": "3"
                    },
                    {
                        "v": 20
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "4"
                    },
                    {
                        "v": 40
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "5"
                    },
                    {
                        "v": 60
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "6"
                    },
                    {
                        "v": 90
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "7"
                    },
                    {
                        "v": 99
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "8"
                    },
                    {
                        "v": 95
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "9"
                    },
                    {
                        "v": 100
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "10"
                    },
                    {
                        "v": 93
                    }
                ]
            }
            ,
            {
                "c": [
                    {
                        "v": "11"
                    },
                    {
                        "v": 91
                    }
                ]
            }

        ]
    };

//    $scope.chartType = 'LineChart';

    $scope.chart = {
        "type":'LineChart',
        "cssStyle": "height:400px; width:100%;border: 1px #ccc solid",
        "data": data,
        "options": {
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
                "title": "Time"
            }
        },
        "formatters": {},
        "displayed": true
    };

    $scope.chartTypes=['PieChart','LineChart','AreaChart','ColumnChart','BarChart','Table'];

    $scope.setChartType= function(index){
        $scope.chart.type = $scope.chartTypes[index];
    };

    $scope.isActiveChartType = function(index){
        if($scope.chartTypes[index] == $scope.chart.type)
            return true;

        return false;
    }
}