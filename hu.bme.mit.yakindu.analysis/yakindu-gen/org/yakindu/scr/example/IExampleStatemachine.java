package org.yakindu.scr.example;

import org.yakindu.scr.IStatemachine;
import org.yakindu.scr.ITimerCallback;

public interface IExampleStatemachine extends ITimerCallback,IStatemachine {

	public interface SCInterface {
	
		public void raiseStart();
		
		public void raiseWhite();
		
		public void raiseBlack();
		
		public long getWhiteTime();
		
		public void setWhiteTime(long value);
		
		public long getBlackTime();
		
		public void setBlackTime(long value);
		
	}
	
	public SCInterface getSCInterface();
	
}
