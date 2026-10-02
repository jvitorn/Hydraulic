package org.geysermc.hydraulic.block;

import org.junit.jupiter.api.Test;
import team.unnamed.creative.blockstate.Condition;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultipartConditionMatcherTest {
    @Test
    void matchesPropertyValue() {
        assertTrue(matches(Condition.match("powered", true), Map.of("powered", "true")));
        assertFalse(matches(Condition.match("powered", false), Map.of("powered", "true")));
    }

    @Test
    void matchesAndCondition() {
        Condition condition = Condition.and(
                Condition.match("north", true),
                Condition.match("up", false)
        );

        assertTrue(matches(condition, Map.of("north", "true", "up", "false")));
        assertFalse(matches(condition, Map.of("north", "true", "up", "true")));
    }

    @Test
    void matchesOrCondition() {
        Condition condition = Condition.or(
                Condition.match("north", true),
                Condition.match("south", true)
        );

        assertTrue(matches(condition, Map.of("north", "false", "south", "true")));
        assertFalse(matches(condition, Map.of("north", "false", "south", "false")));
    }

    @Test
    void matchesNestedCableLikeConditionFromIssue112() {
        Condition condition = Condition.and(
                Condition.or(
                        Condition.match("north", true),
                        Condition.match("south", true)
                ),
                Condition.match("up", false)
        );

        assertTrue(matches(condition, Map.of(
                "north", "true",
                "south", "false",
                "up", "false"
        )));
    }

    @Test
    void treatsMissingPropertyAsNonMatch() {
        assertFalse(matches(Condition.match("north", true), Map.of()));
    }

    @Test
    void noneConditionAlwaysMatches() {
        assertTrue(matches(Condition.NONE, Map.of()));
    }

    private static boolean matches(Condition condition, Map<String, String> properties) {
        return MultipartConditionMatcher.matches(condition, properties::get);
    }
}
