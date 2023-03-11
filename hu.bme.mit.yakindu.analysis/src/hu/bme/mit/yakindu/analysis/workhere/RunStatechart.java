package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		
		s.init();
		s.enter();
		
		s.runCycle();
		print(s);
		
		/*s.raiseStart();
		s.runCycle();
		
		System.in.read();
		
		s.raiseWhite();
		s.runCycle();
		print(s);*/
		
		//3.5.
		Scanner input = new Scanner(System.in);
		String line = input.nextLine();
		while(!line.equals("exit")) {
			if(line.equals("start")) {
				s.raiseStart();
				s.runCycle();
				print(s);
			}else if(line.equals("white")) {
				s.raiseWhite();
				s.runCycle();
				print(s);
			}else if(line.equals("black")) {
				s.raiseBlack();
				s.runCycle();
				print(s);
			}else {
				System.out.println("Wrong input!");
			}
			line = input.nextLine();
		}
		s.exit();
		input.close();
		
		System.exit(0);
	}

	//3.6.
	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
