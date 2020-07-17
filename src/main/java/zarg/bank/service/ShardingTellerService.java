package zarg.bank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("ShardingTellerService")
@Slf4j
class ShardingTellerService implements TellerService {

    private final TellerService tellerService;
    private final List<ExecutorService> executors;

    private final int shards;

    ShardingTellerService(@Qualifier("DefaultTellerService") TellerService tellerService,
                          @Value("${async.shards:50}") int shards) {
        this.tellerService = tellerService;
        this.shards = shards;
        this.executors = IntStream.range(0, shards)
                .mapToObj(i -> Executors.newSingleThreadExecutor())
                .collect(Collectors.toList());
    }

    @Override
    public void credit(String accountBid, BigDecimal amount) {
        runTask(accountBid, () -> tellerService.credit(accountBid, amount));
    }

    @Override
    public void debit(String accountBid, BigDecimal amount) {
        runTask(accountBid, () -> tellerService.debit(accountBid, amount));
    }

    @Override
    public BigDecimal balance(String accountBid) {
        return tellerService.balance(accountBid);
    }

    private void runTask(String accountBid, Runnable runnable) {

        if (shards == 0) {
            runnable.run(); // No Sharding
        } else {

            final int shard = accountBid.hashCode() % shards;
            ExecutorService executorService = executors.get(shard);
            CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, executorService);
            future.whenComplete((result, e) -> {
                if (e != null) {
                    log.error("Shard {} failed.", shard, e);
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
