package org.eclipse.dltk.ruby.typeinference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ddp.AbstractGoal;
import org.eclipse.dltk.ddp.BasicContext;
import org.eclipse.dltk.ddp.ExpressionGoal;
import org.eclipse.dltk.ddp.GoalEvaluator;
import org.eclipse.dltk.ddp.IGoal;
import org.eclipse.dltk.evaluation.types.IEvaluatedType;
import org.eclipse.dltk.ruby.ast.ColonExpression;

public class ColonExpressionEvaluator extends GoalEvaluator {

	private boolean initialized = false;
	
	private List expressionEnding = new ArrayList ();
	
	private AbstractGoal leftPart = null;
	
	private IEvaluatedType resultType = null;
	
	public ColonExpressionEvaluator(IGoal goal) {
		super(goal);
	}
	
	private ColonExpressionGoal getTypedGoal () {
		return (ColonExpressionGoal) this.goal;
	}

	public IGoal produceNextSubgoal(IGoal previousGoal, IEvaluatedType previousResult) {
		if (!initialized) {
			initialize();
		}
		if (previousResult != null) {
			resultType = previousResult;
		} else if (leftPart != null) {
			AbstractGoal g = leftPart;
			leftPart = null;
			return g;
		}
		return null;
	}
	
	private void initialize () {
		BasicContext context = (BasicContext) getGoal().getContext();
		Expression expr = getTypedGoal().getColonExpression();
		while (expr instanceof ColonExpression) {
			ColonExpression colonExpression = (ColonExpression) expr;
			if (colonExpression.isFull()) {
				break;
			} else {
				expressionEnding.add(colonExpression.getName());
			}
			expr = colonExpression.getLeft();
		}
		if (expr instanceof ColonExpression) {
			ColonExpression colonExpression = (ColonExpression) expr;
			leftPart = new ConstantTypeGoal(context, expr.sourceStart(), colonExpression.getName());
		} else  {
			leftPart = new ExpressionGoal(context, expr);
		}
		initialized = true;
	}

	public IEvaluatedType produceType() { //FIXME
		if (resultType != null) {
			String[] leftFQN = null;
			if (resultType instanceof RubyClassType) {
				leftFQN = ((RubyClassType)resultType).getFQN();
			} else if (resultType instanceof RubyMetaClassType) {
				leftFQN = ((RubyClassType) ((RubyMetaClassType)resultType).getInstanceType()).getFQN();
			}
			if (leftFQN != null) {
				String[] newfqn = new String[leftFQN.length + expressionEnding.size()];
				for (int i = 0; i < leftFQN.length; i++) {
					newfqn[i] = leftFQN[i];
				}
				int pos = newfqn.length - 1;
				for (Iterator iter = expressionEnding.iterator(); iter.hasNext();) {
					String element = (String) iter.next();
					newfqn[pos] = element;
					pos--;
				}
				RubyClassType classType = new RubyClassType(newfqn, null, null);
				return new RubyMetaClassType(classType, null);
			}
		}
		return null;
	}

}
