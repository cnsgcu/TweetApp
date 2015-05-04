function FrequencyMeter(meter, chart, url) {
    var self = this instanceof FrequencyMeter
             ? this
             : Object.create(FrequencyMeter.prototype);

    self.threshold = 63;

    self.data = (new Array(self.threshold)).fill(0);
    self.meter = meter;
    self.chart = chart;

    self.sse = new EventSource(url);

    self.sse.addEventListener("message", function (msg) {
        self.data.push(msg.data);

        if (self.data.length > self.threshold) {
            self.data.splice(0, self.data.length - self.threshold);
        }

        self.reChart();
        self.reCount();
    });
}

FrequencyMeter.prototype.reChart = function () {
    this.chart.sparkline(this.data, {type: "bar", barWidth: "2.8", barColor: 'rgb(204,221,255)'});
};

FrequencyMeter.prototype.reCount = function () {
    this.meter.text(this.data[this.data.length - 1]);
};

function CountMeter(chart, url) {
    var self = this instanceof CountMeter
             ? this
             : Object.create(CountMeter.prototype);

    self.threshold = 50;

    self.data = (new Array(self.threshold)).fill(0);
    self.chart = chart;

    self.sse = new EventSource(url);

    self.sse.addEventListener("message", function (msg) {
        self.data.push(msg.data);

        if (self.data.length > self.threshold) {
            self.data.splice(0, self.data.length - self.threshold);
        }

        self.reChart();
    });
}

CountMeter.prototype.reChart = function () {
    this.chart.sparkline(this.data, {type: "line", defaultPixelsPerValue: "2", height: "10px"});
};

function TopMeter(url)
{
    var self = this instanceof TopMeter
             ? this
             : Object.create(TopMeter.prototype);

    self.sse = new EventSource(url);

    self.sse.addEventListener("message", function (msg) {
        console.log(msg.data);
    });
}

// =====================================================================================================================

var tweetFrq = new FrequencyMeter($("#tweet-frq"), $("#tweet-frq-chart"), "/statistic/tweet");
var retweetFrq = new FrequencyMeter($("#retweet-frq"), $("#retweet-frq-chart"), "/statistic/retweet");

// To have more than 6 connections to server change FireFox max persistent connections in about:config
$('#left-analysis').find('span').map(function(idx, dom) {
    return new CountMeter($(dom), "/statistic/tweet/" + $(dom).attr('id'));
});

var top3Devices = new TopMeter("/statistic/device");
var top5Languages = new TopMeter("/statistic/language");
var top5Topics = new TopMeter("/statistic/topic");

var bubbles = [
    //{lat: 39.099727, lng: -92.578567},
    //{lat: 37.099727, lng: -92.578567},
    //{lat: 34.099727, lng: -92.578567},
    //{lat: 32.099727, lng: -92.578567},
    //{lat: 31.099727, lng: -92.578567},
    //{lat: 39.099727, lng: -94.578567}
];

var map = new Datamap({
	element: document.getElementById('map'),
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

map.addPlugin('bigCircle', function ( layer, data ) {
    var self = this;
    var className = 'bigCircle';
    var bubbles = layer.selectAll(className).data( data, JSON.stringify );

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

map.bigCircle( bubbles );

d3.selectAll('path').style('fill', '#000');
d3.selectAll('path').style('stroke', '#222');

//var dumPie = [60, 30, 10];
//$(".device-pie").sparkline(dumPie, {type: "pie", width: "50px", height: "50px", offset: "-90"});