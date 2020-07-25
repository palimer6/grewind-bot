package net.grewind.palimer.bot.logging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ListHandler<S> {
    public final List<S> MESSAGE_LIST = new ArrayList<>();

    public synchronized <T, R> R syncedFunction(T input, @NotNull Function<T, R> function) {
        return function.apply(input);
    }

    public <T> void syncedConsumer(T input, Consumer<T> consumer) {
        syncedFunction(input, (Function<T, Void>) t -> {
            consumer.accept(t);
            return null;
        });
    }

    public <T> T syncedSupplier(Supplier<T> supplier) {
        return syncedFunction(null, (Function<Void, T>) aVoid -> supplier.get());
    }

    public void syncedRunnable(Runnable runnable) {
        syncedFunction(null, (Function<Void, Void>) aVoid -> {
            runnable.run();
            return null;
        });
    }
}
