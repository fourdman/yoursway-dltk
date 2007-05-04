package org.eclipse.dltk.rhino.dbgp;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class BreakPoint {

	static int last_id = 0;

	protected final String file;
	protected final int line;
	protected final int id;
	protected boolean enabled = true;
	protected boolean isTemporary = false;
	protected int hitValue = 0;
	protected int hitCondition = 0;
	protected	   int currentHitCount=0;
	protected String expression;
	protected boolean isExitBreakpoint;
	protected String method;
	protected boolean isReturn;

	protected BreakPoint(HashMap options) {
		
		if (options.get("-t").equals("return")){
			method=(String) options.get("-m");
			this.isReturn=true;
		}
		try {
			this.file = new File(new URI((String) options.get("-f")))
					.getAbsolutePath().replace('\\', '/');
		} catch (URISyntaxException e) {
			throw new RuntimeException();
		}
		this.line = Integer.parseInt((String) options.get("-n")) - 1;
		String tm = (String) options.get("-r");
		if (tm != null) {
			isTemporary = tm.equals("1");
		}
		String hitValue = (String) options.get("-h");
		if (hitValue != null) {
			this.hitValue = Integer.parseInt(hitValue);
		}
		String hitCondition = (String) options.get("-o");
		setHitCondition(hitCondition);
		expression = (String) options.get("--");
		String disable=(String) options.get("-s");
		if (disable.equals("disabled")){
			this.setEnabled(false);
		}
		if (expression!=null) 
		{
			expression=Base64Helper.decodeString(expression);			
		}
		this.id = last_id++;
	}

	protected void setHitCondition(String hitCondition) {		
		if (hitCondition != null) {
			if (hitCondition.equals(">=")){
				this.hitCondition = 1;	
			}
			if (hitCondition.equals("==")){
				this.hitCondition = 2;	
			}
			if (hitCondition.equals("%")){
				this.hitCondition = 3;	
			}			
		}
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + line;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BreakPoint other = (BreakPoint) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (line != other.line)
			return false;
		return true;
	}

	protected boolean isEnabled() {
		return enabled;
	}

	protected void setEnabled(boolean enabled) {		
		this.enabled = enabled;		
	}
}