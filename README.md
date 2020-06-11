# log-errors

## Installation
`git clone https://github.com/cloudhubs/log-errors.git`

[Install Lombok for your IDE](https://www.baeldung.com/lombok-ide)

## Request
```
curl --location --request POST 'localhost:8080/<route>' --header 'Content-Type: application/json' --data-raw "{\"pathToLogFile\": \"$1\", \"pathToLogDirectory\": \"$2\"}
```

## API
>`/errors/`

Returns JSON formatted parsed error log.

#### Example
```
{
        "errorMessage" : "AttributeError: 'NoneType' object has no attribute 'split'\n",
                "gitHub" : null,
                "isExternal" : false,
                "lineNumber" : 2666,
                "nestedError" : null,
                "source" : "../logscraper/logs/from_prod_anonymized/ccx_data_pipeline_1_anonymized.log",
                "stackOverflow" : null,
                "traceBacks" : [
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 962, in run\nresult = DELEGATES[component].process(broker)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 681, in process\nreturn self.invoke(broker)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/plugins.py\", line 64, in invoke\nreturn super(PluginType, self).invoke(broker)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/insights/core/dr.py\", line 661, in invoke\nreturn self.component(*args)",
                        "File \"/opt/app-root/lib/python3.6/site-packages/ccx_ocp_core/models/nodes.py\", line 108, in Nodes\nint(node.q.status.capacity.memory.value.split(\"Ki\")[0]) / (1000 * 1000), 2",
                        "AttributeError: 'NoneType' object has no attribute 'split'\n"
                ]
}
```

