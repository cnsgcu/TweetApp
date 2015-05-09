function Application(url)
{
    var self = this instanceof Application
             ? this
             : Object.create(Application.prototype);

    self.frqThreshold = 63;
    self.countThreshold = 50;

    self.tweetMeter = $("#tweet-frq");
    self.tweetMeterChart = $("#tweet-frq-chart");
    self.tweetFrqHist = (new Array(self.frqThreshold)).fill(0);

    self.retweetMeter = $("#retweet-frq");
    self.retweetMeterChart = $("#retweet-frq-chart");
    self.retweetFrqHist = (new Array(self.frqThreshold)).fill(0);

    self.devicePie = $("#device-chart");

    self.topicRank = $("#topic-rank");
    self.languageRank = $("#language-rank");

    self.tweetCountMap = $('#left-analysis').find('span').toArray()
                                            .map(function(dom) {
                                                return $(dom).attr('id');
                                            })
                                            .reduce(function(map, state) {
                                                map[state] = {
                                                    chart: $("#" + state),
                                                    data: (new Array(self.countThreshold)).fill(0)
                                                };

                                                return map;
                                            }, {});

    self.sse = new EventSource(url);

    self.sse.addEventListener("tweet_frq", function (msg) {
        var frq = msg.data;

        self.tweetFrqHist.push(frq);

        if (self.tweetFrqHist.length > self.frqThreshold) {
            self.tweetFrqHist.splice(0, self.tweetFrqHist.length - self.frqThreshold);
        }

        self.tweetMeter.text(frq);
        self.tweetMeterChart.sparkline(self.tweetFrqHist, {type: "bar", barWidth: "2.8", barColor: 'rgb(204,221,255)'});
    });

    self.sse.addEventListener("retweet_frq", function (msg) {
        var frq = msg.data;

        self.retweetFrqHist.push(frq);

        if (self.retweetFrqHist.length > self.frqThreshold) {
            self.retweetFrqHist.splice(0, self.retweetFrqHist.length - self.frqThreshold);
        }

        self.retweetMeter.text(frq);
        self.retweetMeterChart.sparkline(self.retweetFrqHist, {type: "bar", barWidth: "2.8", barColor: 'rgb(204,221,255)'});
    });

    self.sse.addEventListener("device_rank", function (msg) {
        var data = JSON.parse(msg.data)
                       .sort(function (x, y) {
                           return -(x["count"] - y["count"]);
                       })
                       .map(function (d) {
                           return d["count"];
                       });

        self.devicePie.sparkline(data, {type: "pie", width: "50px", height: "50px", offset: "-90"});
    });

    self.sse.addEventListener("topic_rank", function (msg) {
        var data = JSON.parse(msg.data).sort(function(x,y) {
            return -(x["count"] - y["count"]);
        });

        var divs = data.map(function (d, i) {
            return '<div style="margin: 2px 0;">' + (i + 1) + '. ' + d["topic"] + '</div>';
        }).join('\n');

        self.topicRank.html(divs);
    });

    self.sse.addEventListener("language_rank", function (msg) {
        var data = JSON.parse(msg.data).sort(function(x,y) {
            return -(x["count"] - y["count"]);
        });

        var divs = data.map(function (d, i) {
            return '<div style="margin: 2px 0;">' + (i + 1) + '. ' + d["language"] + '</div>';
        }).join('\n');

        self.languageRank.html(divs);
    });

    self.sse.addEventListener("state_tweet_count", function (msg) {
        var data = JSON.parse(msg.data),
            stateId = data["state"].replace(" ", "_"),
            chart = self.tweetCountMap[stateId].chart,
            chartData = self.tweetCountMap[stateId].data;

        chartData.push(data["count"]);

        if (chartData.length > self.countThreshold) {
            chartData.splice(0, chartData.length - self.countThreshold);
        }

        chart.sparkline(chartData, {type: "line", defaultPixelsPerValue: "2", height: "10px"});
    });
}

function TweetMap(containerId, url)
{
    var self = this instanceof TweetMap
        ? this
        : Object.create(TweetMap.prototype);

    self.bubbleThreshold  = 1000;
    self.bubbles = [];

    self.tweetMap = new Datamap({
        element: document.getElementById(containerId),
        fills: {
            defaultFill: "#000"
        },
        geographyConfig: {
            dataUrl: null, //if not null, datamaps will fetch the map JSON (currently only supports topojson)
            hideAntarctica: true,
            borderWidth: 1,
            borderColor: '#222',
            popupTemplate: function(geography, data) { //this function should just return a string
                return '<div class="hoverinfo"><strong>' + geography.properties.name + '</strong></div>';
            },
            popupOnHover: true, //disable the popup while hovering
            highlightOnHover: true,
            highlightFillColor: '#000',
            highlightBorderColor: '#333',
            highlightBorderWidth: 2
        },
        scope: 'usa'
    });

    self.tweetMap.addPlugin('bigCircle', function ( layer, data ) {
        var self = this,
            className = 'bigCircle',
            bubbles = layer.selectAll(className).data( data, JSON.stringify );

        bubbles.enter()
            .append('circle')
            .attr('class', className)
            .attr('cx', function ( datum ) {
                return self.latLngToXY(datum.lat, datum.lng)[0];
            })
            .attr('cy', function ( datum ) {
                return self.latLngToXY(datum.lat, datum.lng)[1];
            })
            .attr('r', 1.5);
    });

    self.tweetMap.bigCircle( self.bubbles );

    d3.selectAll('path').style('fill', '#000');
    d3.selectAll('path').style('stroke', '#222');

    self.sse = new EventSource(url);

    self.sse.addEventListener("message", function (msg) {
        var point = JSON.parse(msg.data);

        self.bubbles.push(point);
        if (self.bubbles.length > self.bubbleThreshold) {
            self.bubbles.splice(0, self.bubbles.length - self.bubbleThreshold);
        }

        self.tweetMap.bigCircle( self.bubbles );
    });
}

var tweetMap = new TweetMap("map", "/tweet_point");
var app = new Application("/statistic");