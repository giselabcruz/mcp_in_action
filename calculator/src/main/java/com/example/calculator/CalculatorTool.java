package com.example.calculator;

import org.springframework.ai.mcp.tool.Tool;
import org.springframework.stereotype.Component;

@Component
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
		if (b == 0) {
			throw new IllegalArgumentException("Cannot divide by zero");
		}
		return a / b;
	}

	@Tool
	public double modulus(double a, double b) {
		return a % b;
	}

	@Tool
	public double power(double base, double exponent) {
		return Math.pow(base, exponent);
	}

	@Tool
	public double squareRoot(double number) {
		if (number < 0) {
			throw new IllegalArgumentException("Cannot take square root of a negative number");
		}
		return Math.sqrt(number);
	}

	@Tool
	public double absolute(double number) {
		return Math.abs(number);
	}
}