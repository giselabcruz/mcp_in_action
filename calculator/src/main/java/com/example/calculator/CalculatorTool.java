package com.example.calculator;

import org.springframework.stereotype.Service;
import org.springframework.ai.tool.annotation.Tool;

@Service
public class CalculatorTool {

	public CalculatorTool() {
		System.out.println("✅ CalculatorTool loaded");
	}

	@Tool
	public double add(double a, double b) {
		return a + b;
	}

	@Tool
	public double subtract(double a, double b) {
		return a - b;
	}

	@Tool
	public double multiply(double a, double b) {
		return a * b;
	}

	@Tool
	public double divide(double a, double b) {
		if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
		return a / b;
	}
}