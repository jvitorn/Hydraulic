package org.geysermc.hydraulic.block;

import team.unnamed.creative.blockstate.Condition;

import java.util.Objects;
import java.util.function.Function;

/** Evaluates condition trees without depending on Minecraft block instances. */
final class MultipartConditionMatcher {
    private MultipartConditionMatcher() {
    }

    // The lookup returns null for properties absent from the current block state.
    static boolean matches(Condition condition, Function<String, String> propertyValue) {
        if (condition == Condition.NONE) {
            return true;
        }
        if (condition instanceof Condition.Match match) {
            return Objects.equals(propertyValue.apply(match.key()), match.value().toString());
        }
        if (condition instanceof Condition.And andCondition) {
            return andCondition.conditions().stream().allMatch(child -> matches(child, propertyValue));
        }
        if (condition instanceof Condition.Or orCondition) {
            return orCondition.conditions().stream().anyMatch(child -> matches(child, propertyValue));
        }
        return false;
    }
}
