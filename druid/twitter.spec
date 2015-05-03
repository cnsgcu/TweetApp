[
  {
    "dataSchema" : {
      "dataSource" : "twitter",
      "parser" : {
        "type" : "string",
        "parseSpec" : {
          "format" : "json",
          "timestampSpec" : {
            "column" : "timestamp",
            "format" : "auto"
          },
          "dimensionsSpec" : {
            "dimensions": [
              "timestamp",
              "lat",
              "lon",
              "language",
              "state",
              "device",
              "retweetCount",
              "topic"
            ],
            "dimensionExclusions" : [],
            "spatialDimensions" : [
              {
                "dimName": "geo",
                "dims": [
                  "lat",
                  "lon"
                ]
              }
            ]
          }
        }
      },
      "metricsSpec" : [{
        "type" : "count",
        "name" : "tweets"
      }, {
        "type" : "longSum",
        "name" : "total_retweet_count",
        "fieldName" : "retweetCount"
      }],
      "granularitySpec" : {
        "type" : "uniform",
        "segmentGranularity" : "DAY",
        "queryGranularity" : "NONE"
      }
    },
    "ioConfig" : {
      "type" : "realtime",
      "firehose": {
        "type": "kafka-0.8",
        "consumerProps": {
          "zookeeper.connect": "localhost:2181",
          "zookeeper.connection.timeout.ms" : "15000",
          "zookeeper.session.timeout.ms" : "15000",
          "zookeeper.sync.time.ms" : "5000",
          "group.id": "druid-example",
          "fetch.message.max.bytes" : "1048586",
          "auto.offset.reset": "largest",
          "auto.commit.enable": "false"
        },
        "feed": "tweet"
      },
      "plumber": {
        "type": "realtime"
      }
    },
    "tuningConfig": {
      "type" : "realtime",
      "maxRowsInMemory": 500000,
      "intermediatePersistPeriod": "PT10m",
      "windowPeriod": "PT10m",
      "basePersistDirectory": "\/tmp\/realtime\/basePersist",
      "rejectionPolicy": {
        "type": "messageTime"
      }
    }
  }
]
