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
		balls[0] = new Ball(width / 3 - 1, height * 0.9, 1.2, 1.6, 0.2);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3);
	}

	void step(double deltaT) {
		boolean hasColided = false;
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
			double offset2 = 0.01;
			for (Ball a : balls) {
				if (a != b && !hasColided)
					if (Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)) <= a.radius + b.radius) {
						hasColided = true;
						
						Ball olda = a.copy();
						Ball oldb = b.copy();
						a.bounce(oldb);
						b.bounce(olda);
					}
			}
		}

	}

	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		/**
		 * Position, speed, and radius of the ball. You may wish to add other
		 * attributes.
		 */
		double x, y, vx, vy, radius;

		Ball(double x, double y, double vx, double vy, double r) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
		}

		public Ball copy() {
			return new Ball(x, y, vx, vy, radius);
		}

		public double getMass() {
			return radius * radius * Math.PI;
		}

		public void bounce(Ball ball) {
			Vector2 vel1 = new Vector2(vx, vy);
			Vector2 vel2 = new Vector2(ball.vx, ball.vy);
			Vector2 pos1 = new Vector2(x, y);
			Vector2 pos2 = new Vector2(ball.x, ball.y);
			double mass1 = getMass();
			double mass2 = ball.getMass();
			
			double massScalar = 2 * mass2 / (mass1 + mass2);
			double vectorScalar = (vel1.subtract(vel2)).dotProduct(pos1.subtract(pos2)) / Math.pow(pos2.subtract(pos1).length(), 2);

			Vector2 newVel = vel1.subtract(pos1.subtract(pos2).scale(massScalar * vectorScalar));
			vx = newVel.x;
			vy = newVel.y;
		}

		// Solution based on our linear algebra
		public void bounce2(Ball ball) {
			Vector2 vel1 = new Vector2(vx, vy);
			Vector2 vel2 = new Vector2(ball.vx, ball.vy);
			Vector2 pos1 = new Vector2(x, y);
			Vector2 pos2 = new Vector2(ball.x, ball.y);
			double mass1 = getMass();
			double mass2 = ball.getMass();

			// No setup anymore!
			Vector2 diffVec = pos1.subtract(pos2);
			Vector2 base1 = diffVec.scale(1/diffVec.length());
			Vector2 base2 = new Vector2(x, y);
		}
	}

	public class Vector2 {
		protected double x,y;

		Vector2(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public Vector2 copy() {
			return new Vector2(x, y);
		}

		public double dotProduct (Vector2 vec) {
			return x * vec.x + y * vec.y;
		}

		public double length() {
			return Math.sqrt(x*x + y*y);
		}

		public Vector2 add(Vector2 vec) {
			return new Vector2(x + vec.x, y + vec.y);
		}

		public Vector2 subtract(Vector2 vec) {
			return new Vector2(x - vec.x, y - vec.y);
		}

		public Vector2 scale(double scalar) {
			return new Vector2(x*scalar, y*scalar);
		}

		public Vector2 rotate(Vector2 a, double d) {
			d = 2*Math.PI - d;
			a.x = a.x * Math.cos(d) - a.y * Math.sin(d);
			a.y = a.x * Math.sin(d) + a.y * Math.cos(d);
			return a;
		}
	}
}