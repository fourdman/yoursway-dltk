package org.eclipse.dltk.dbgp.breakpoints;

public class DbgpBreakpointConfig {
	private static final String TEMPORARY_TRUE = "1";
	private static final String TEMPORARY_FALSE = "0";

	private static final String STATE_ENABLED = "enabled";
	private static final String STATE_DISABLED = "disabled";

	private static final String HIT_CONDITION_GREATER = ">=";
	private static final String HIT_CONDITION_EQUAL = "==";
	private static final String HIT_CONDITION_MULTIPLE = "%";

	private boolean enabled;

	private boolean temporary;

	private int hitValue;

	private int hitCondition;

	public DbgpBreakpointConfig(boolean enabled) {
		this(enabled, -1, -1);
	}

	public DbgpBreakpointConfig(boolean enabled, int hitValue, int hitCondition) {
		this(enabled, -1, -1, false);
	}

	public DbgpBreakpointConfig(boolean enabled, int hitValue,
			int hitCondition, boolean temporary) {
		this.enabled = enabled;
		this.hitValue = hitValue;
		this.hitCondition = hitCondition;
		this.temporary = temporary;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public int getHitValue() {
		return hitValue;
	}

	public int getHitCondition() {
		return hitCondition;
	}

	public String getTemporaryString() {
		return temporary ? TEMPORARY_TRUE : TEMPORARY_FALSE;
	}

	public String getStateString() {
		return enabled ? STATE_ENABLED : STATE_DISABLED;
	}

	public String getHitConditionString() {
		if (hitCondition == IDbgpBreakpoint.HIT_CONDITION_EQUAL) {
			return HIT_CONDITION_EQUAL;
		} else if (hitCondition == IDbgpBreakpoint.HIT_CONDITION_GREATER) {
			return HIT_CONDITION_GREATER;
		} else if (hitCondition == IDbgpBreakpoint.HIT_CONDITION_MULTIPLE) {
			return HIT_CONDITION_MULTIPLE;
		}

		return null;
	}
}
