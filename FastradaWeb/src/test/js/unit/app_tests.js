describe('Controllers', function () {
    describe('users loads page', function () {
        var scope, ctrl;

        beforeEach(inject(function ($controller, $rootScope) {
            scope = $rootScope.$new();
            ctrl = $controller('HomeController', {$scope: scope});
        }));

        it('should be 12', function () {
            expect(scope.sessions.length).toEqual(12);
        });
    });

    describe('send Data to graph maker', function () {
        var resultData = {
            "cols": [
                {
                    "id": "seconds",
                    "label": "Seconds",
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
                        { "v": "1393405044" },
                        {"v": 100 }
                    ]
                },
                {
                    "c": [
                        { "v": "1393405072" },
                        {"v": 110 }
                    ]
                },
                {
                    "c": [
                        { "v": "1393405076" },
                        {"v": 120 }
                    ]
                },
                {
                    "c": [
                        { "v": "1393405080" },
                        {"v": 123 }
                    ]
                },
                {
                    "c": [
                        { "v": "1393405086" },
                        {"v": 99 }
                    ]
                }
            ]
        };

        var rawJson = [
            {    'timestamp': '1393405044',
                'value': '100'
            },
            {    'timestamp': '1393405072',
                'value': '110'
            },
            {    'timestamp': '1393405076',
                'value': '120'
            },
            {    'timestamp': '1393405080',
                'value': '123'
            },
            {    'timestamp': '1393405086',
                'value': '99'
            }
        ];
        var scope, ctrl;
        var params = {'sessionId': '0', 'parameterName': 'Speed'};
        beforeEach(inject(function ($rootScope, $controller) {
            scope = $rootScope.$new();
            ctrl = $controller('SessionDetailController', {$scope: scope, $routeParams: params});
        }));

        it('should return graph data with 5 points', function () {
            scope.createGraphData(rawJson);
            expect(scope.chart.data.rows.length).toBe(5);
        });

        it('should have the same data as our resultData', function () {
            scope.createGraphData(rawJson);
            expect(scope.chart.data == resultData);
        });
    });
})
;