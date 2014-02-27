describe('Controllers', function () {
    describe('users loads page', function () {
        var scope, ctrl;

        beforeEach(inject(function ($controller, $rootScope) {
            scope = $rootScope.$new();
            ctrl = $controller('HomeController', {$scope: scope});
        }));

        it('should contain 12 sessions', function () {
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
        var scope, ctrl, session, $httpBackend;
        var params = {'sessionId': '0', 'parameterName': 'Speed'};
        beforeEach(module('fastrada.services'));

        beforeEach(inject(function ($rootScope, $controller, SessionData, _$httpBackend_) {
            scope = $rootScope.$new();
            session = SessionData;
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET("/api/sessions").respond({});

            ctrl = $controller('SessionDetailController', {$scope: scope, $routeParams: params, SessionData: session});
        }));

        it('should return graph data with 5 points', function () {
            scope.createGraphData(rawJson);
            expect(scope.chart.data.rows.length).toBe(5);
        });

        it('should have the same data as our resultData', function () {
             scope.createGraphData(rawJson);
            expect(scope.chart.data == resultData);
        });

        it('should display 500 chartpoints', function () {
            scope.random500Data();
            expect(scope.chart.data.rows.length).toBe(500);
        });

        it('should return 100 chartpoints', function () {
            scope.chartMinMax.max = 100;
            scope.updateGraph(scope.chartMinMax.min, scope.chartMinMax.max);
            expect(scope.chart.data.rows.length).toBe(102);
        });
    });
})
;