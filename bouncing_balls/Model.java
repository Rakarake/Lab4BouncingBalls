package bouncing_balls;

import java.util.ArrayList;
import java.util.List;

/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish,
 * you can improve the design.
 * 
 * @author Simon Robillard
 *
 */

class Model {

	double areaWidth, areaHeight;

	Ball[] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;

		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 3-1, height * 0.9-1, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT
		for (Ball b : balls) {
			double offset = 0.005;
			// detect collision with the border
			if (b.x < b.radius || b.x > areaWidth - b.radius) {
				b.vx *= -1; // change direction of ball
				b.x += offset * Math.signum(b.vx);
			}
			if (b.y < b.radius || b.y > areaHeight - b.radius) {
				b.vy *= -1;
				b.y += offset * Math.signum(b.vy);
			}

			// compute new position according to the speed of the ball
			b.vx += deltaT * 0;
			b.vy += deltaT * (-1.82);
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;

			// Check if collisionj

			for (Ball a : balls) {
				if (a != b)
					if (Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)) <= a.radius + b.radius) {
						System.out.println("WOW");

					}
			}
		}

	}

	void rotateVelocity(Ball b, double d) {
		rotate(new Vector2(b.vx, b.vy), d);
	}

	Vector2 rotate(Vector2 a, double d) {
		a.x = a.x * Math.cos(d) - a.y * Math.sin(d);
		a.y = a.x * Math.sin(d) + a.y * Math.cos(d);
		return a;
	}

	/**
	 * Simple inner class describing balls.
	 */
	class Ball {

		Ball(double x, double y, double vx, double vy, double r) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
		}

		public double getMass() {
			return radius * radius * Math.PI;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other
		 * attributes.
		 */
		double x, y, vx, vy, radius;
	}

	class Vector2 {
		Vector2(double x, double y) {
			this.x = x;
			this.y = y;
		}

		Vector2(Vector2 old) {
			this.x = old.x;
			this.y = old.y;
		}

		double x, y;
	}
}