package org.eclipse.dltk.ruby.typeinference;

import org.eclipse.dltk.evaluation.types.UnknownType;
import org.eclipse.dltk.ti.ISourceModuleContext;
import org.eclipse.dltk.ti.goals.GoalEvaluator;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.dltk.ti.types.IEvaluatedType;

public class ConstantReferenceEvaluator extends GoalEvaluator {

	private final int STATE_NOT_FOUND = -1;

	private int state = 0;

	// private IEvaluatedType[] evaluatedTypes;
	//	
	// private int countOfEvaluatedTypes;

	private IEvaluatedType result;

	public ConstantReferenceEvaluator(IGoal goal) {
		super(goal);
	}

	public IGoal produceNextSubgoal(IGoal previousGoal, Object previousResult) {
		if (state == 0) {
			initialize();
			state = 1;
		}
		return null;
	}

	private void initialize() {
		ISourceModuleContext typedContext = getTypedContext();
		ConstantTypeGoal typedGoal = getTypedGoal();
		result = RubyTypeInferencingUtils.evaluateConstantType(typedContext.getSourceModule(),
				typedContext.getRootNode(), typedGoal.getOffset(), typedGoal.getName());
		if (result == null)
			result = UnknownType.INSTANCE;
	}

	private ConstantTypeGoal getTypedGoal() {
		return (ConstantTypeGoal) goal;
	}

	private ISourceModuleContext getTypedContext() {
		return (ISourceModuleContext) goal.getContext();
	}

	public Object produceResult() {
		if (state == 0) {
			initialize();
			state = 1;
		}
		return result; // RubyTypeInferencingUtils.combineTypes(evaluatedTypes);
	}

}
