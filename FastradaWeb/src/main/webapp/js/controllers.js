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
}

function SessionDetailController($scope) {
    $scope.chart = {
        "type": "ColumnChart",
        "cssStyle": "height:400px; width:600px;border: 1px #ccc solid",
        "data": {
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
        },
        "options": {
            "title": "Temperature",
            "isStacked": "true",
            "fill": 20,
            "displayExactValues": true,
            "vAxis": {
                "title": "Temperature in °C",
                "gridlines": {
                    "count": 6
                }
            },
            "hAxis": {
                "title": "Time"
            }
        },
        "formatters": {},
        "displayed": true
    }
}