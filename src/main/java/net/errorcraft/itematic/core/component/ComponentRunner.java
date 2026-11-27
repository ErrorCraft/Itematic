package net.errorcraft.itematic.core.component;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;

import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

public class ComponentRunner<R> {
    private final DataComponentGetter components;
    private final BooleanSupplier haltCondition;
    private boolean finished;
    private final BinaryOperator<R> resultMerger;
    private R result;

    public ComponentRunner(DataComponentGetter components, BooleanSupplier haltCondition, BinaryOperator<R> resultMerger, R initialValue) {
        this.components = components;
        this.haltCondition = haltCondition;
        this.finished = haltCondition.getAsBoolean();
        this.resultMerger = resultMerger;
        this.result = initialValue;
    }

    public R result() {
        return this.result;
    }

    public <C> ComponentRunner<R> runFinishing(DataComponentType<C> type, Function<C, R> applier) {
        return this.run(
            type,
            _ -> true,
            applier,
            _ -> true
        );
    }

    public <C> ComponentRunner<R> runFinishing(DataComponentType<C> type, Predicate<C> mayRun, Function<C, R> applier) {
        return this.run(
            type,
            mayRun,
            applier,
            _ -> true
        );
    }

    public <C> ComponentRunner<R> run(DataComponentType<C> type, Function<C, R> applier, Predicate<R> shouldFinish) {
        return this.run(
            type,
            _ -> true,
            applier,
            shouldFinish
        );
    }

    public <C> ComponentRunner<R> run(DataComponentType<C> type, Predicate<C> mayRun, Function<C, R> runner, Predicate<R> shouldFinish) {
        // Test whether we can run components in the first place
        if (this.finished) {
            return this;
        }

        C component = this.components.get(type);
        // Test whether we can run the component based on the contents of the component itself
        if (component == null || !mayRun.test(component)) {
            return this;
        }

        R result = runner.apply(component);
        // Test whether we should mark this runner as finished by
        // - Checking whether the outside context prevents this runner from running additional components
        // - Checking whether the result for this component should consider this component as the last one to run
        //   - This can be used to finish early or pass through to the next component for specific results
        if (this.haltCondition.getAsBoolean() || shouldFinish.test(result)) {
            this.finished = true;
        }

        this.result = this.resultMerger.apply(this.result, result);
        return this;
    }
}
