package org.yakindu.scr.example;
import org.yakindu.scr.ITimer;

public class ExampleStatemachine implements IExampleStatemachine {

	protected class SCInterfaceImpl implements SCInterface {
	
		private boolean start;
		
		public void raiseStart() {
			start = true;
		}
		
		private boolean white;
		
		public void raiseWhite() {
			white = true;
		}
		
		private boolean black;
		
		public void raiseBlack() {
			black = true;
		}
		
		private long whiteTime;
		
		public long getWhiteTime() {
			return whiteTime;
		}
		
		public void setWhiteTime(long value) {
			this.whiteTime = value;
		}
		
		private long blackTime;
		
		public long getBlackTime() {
			return blackTime;
		}
		
		public void setBlackTime(long value) {
			this.blackTime = value;
		}
		
		protected void clearEvents() {
			start = false;
			white = false;
			black = false;
		}
	}
	
	protected SCInterfaceImpl sCInterface;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_Init,
		main_region_Black,
		main_region_White,
		$NullState$
	};
	
	private final State[] stateVector = new State[1];
	
	private int nextStateIndex;
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[2];
	public ExampleStatemachine() {
		sCInterface = new SCInterfaceImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		for (int i = 0; i < 1; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		sCInterface.setWhiteTime(60);
		
		sCInterface.setBlackTime(60);
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_main_region_default();
	}
	
	public void exit() {
		exitSequence_main_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_Init:
			return stateVector[0] == State.main_region_Init;
		case main_region_Black:
			return stateVector[0] == State.main_region_Black;
		case main_region_White:
			return stateVector[0] == State.main_region_White;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correct
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
	}
	
	public SCInterface getSCInterface() {
		return sCInterface;
	}
	
	public void raiseStart() {
		sCInterface.raiseStart();
	}
	
	public void raiseWhite() {
		sCInterface.raiseWhite();
	}
	
	public void raiseBlack() {
		sCInterface.raiseBlack();
	}
	
	public long getWhiteTime() {
		return sCInterface.getWhiteTime();
	}
	
	public void setWhiteTime(long value) {
		sCInterface.setWhiteTime(value);
	}
	
	public long getBlackTime() {
		return sCInterface.getBlackTime();
	}
	
	public void setBlackTime(long value) {
		sCInterface.setBlackTime(value);
	}
	
	private boolean check_main_region_Init_tr0_tr0() {
		return sCInterface.start;
	}
	
	private boolean check_main_region_Black_tr0_tr0() {
		return sCInterface.black;
	}
	
	private boolean check_main_region_Black_tr1_tr1() {
		return timeEvents[0];
	}
	
	private boolean check_main_region_White_tr0_tr0() {
		return sCInterface.white;
	}
	
	private boolean check_main_region_White_tr1_tr1() {
		return timeEvents[1];
	}
	
	private void effect_main_region_Init_tr0() {
		exitSequence_main_region_Init();
		enterSequence_main_region_White_default();
	}
	
	private void effect_main_region_Black_tr0() {
		exitSequence_main_region_Black();
		enterSequence_main_region_White_default();
	}
	
	private void effect_main_region_Black_tr1() {
		exitSequence_main_region_Black();
		sCInterface.setBlackTime(sCInterface.getBlackTime() - 1);
		
		enterSequence_main_region_Black_default();
	}
	
	private void effect_main_region_White_tr0() {
		exitSequence_main_region_White();
		enterSequence_main_region_Black_default();
	}
	
	private void effect_main_region_White_tr1() {
		exitSequence_main_region_White();
		sCInterface.setWhiteTime(sCInterface.getWhiteTime() - 1);
		
		enterSequence_main_region_White_default();
	}
	
	/* Entry action for state 'Black'. */
	private void entryAction_main_region_Black() {
		timer.setTimer(this, 0, 1 * 1000, false);
	}
	
	/* Entry action for state 'White'. */
	private void entryAction_main_region_White() {
		timer.setTimer(this, 1, 1 * 1000, false);
	}
	
	/* Exit action for state 'Black'. */
	private void exitAction_main_region_Black() {
		timer.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'White'. */
	private void exitAction_main_region_White() {
		timer.unsetTimer(this, 1);
	}
	
	/* 'default' enter sequence for state Init */
	private void enterSequence_main_region_Init_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Init;
	}
	
	/* 'default' enter sequence for state Black */
	private void enterSequence_main_region_Black_default() {
		entryAction_main_region_Black();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Black;
	}
	
	/* 'default' enter sequence for state White */
	private void enterSequence_main_region_White_default() {
		entryAction_main_region_White();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_White;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* Default exit sequence for state Init */
	private void exitSequence_main_region_Init() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Black */
	private void exitSequence_main_region_Black() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_Black();
	}
	
	/* Default exit sequence for state White */
	private void exitSequence_main_region_White() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_main_region_White();
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_Init:
			exitSequence_main_region_Init();
			break;
		case main_region_Black:
			exitSequence_main_region_Black();
			break;
		case main_region_White:
			exitSequence_main_region_White();
			break;
		default:
			break;
		}
	}
	
	/* The reactions of state Init. */
	private void react_main_region_Init() {
		if (check_main_region_Init_tr0_tr0()) {
			effect_main_region_Init_tr0();
		}
	}
	
	/* The reactions of state Black. */
	private void react_main_region_Black() {
		if (check_main_region_Black_tr0_tr0()) {
			effect_main_region_Black_tr0();
		} else {
			if (check_main_region_Black_tr1_tr1()) {
				effect_main_region_Black_tr1();
			}
		}
	}
	
	/* The reactions of state White. */
	private void react_main_region_White() {
		if (check_main_region_White_tr0_tr0()) {
			effect_main_region_White_tr0();
		} else {
			if (check_main_region_White_tr1_tr1()) {
				effect_main_region_White_tr1();
			}
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_Init_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_Init:
				react_main_region_Init();
				break;
			case main_region_Black:
				react_main_region_Black();
				break;
			case main_region_White:
				react_main_region_White();
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
}
