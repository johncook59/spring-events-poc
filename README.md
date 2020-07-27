# Spring Events POC
Demonstrates how publishing Spring Application Events can be used to decouple logging and Datadog/StatsD metrics from core application code

# View Metrics
Run Graphite in Docker to see events being graphed using this command
```
% docker run -d \
 --name graphite \
 --restart=always \
 -p 81:80 \
 -p 2003-2004:2003-2004 \
 -p 2023-2024:2023-2024 \
 -p 8125:8125/udp \
 -p 8126:8126 \
 graphiteapp/graphite-statsd
```

View the graphs on the [Graphite UI](http://localhost:81/?showTarget=stats.spring_events_poc.item.update.success&width=1083&height=970&from=-5minutes&fontSize=15&target=stats.spring_events_poc.item.insert.rolledback&target=stats.spring_events_poc.item.insert.success&target=stats.spring_events_poc.item.update.rolledback&target=stats.spring_events_poc.item.update.success)
