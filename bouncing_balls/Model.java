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

public class Model {

	double areaWidth, areaHeight;

	Ball[] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;

		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 2, height * 0.5, 0.6, -1.2, 0.2);
		balls[1] = new Ball(width / 2, height * 0.2, -0.3, 2.4, 0.3);
	}

	void step(double deltaT) {
		boolean hasColided = false;
		for (Ball b : balls) {
			// We check for collisions "next" step, so that the balls don't end up inside eachother and the walls.
			// This is fine since deltaT is small.
			Ball bNext = b.copy();
			accelerate(bNext, deltaT);
			moveBall(bNext, deltaT);

			// Check wall collision
			if (bNext.x < bNext.radius || bNext.x > areaWidth - bNext.radius) {
				b.vx *= -1; 
				moveBall(b, deltaT);
				continue;
			}
			if (bNext.y < bNext.radius || bNext.y > areaHeight - bNext.radius) {
				b.vy *= -1;
				moveBall(b, deltaT);
				continue;
			}

			// Check if balls collide
			for (Ball a : balls) {
				Ball aNext = a.copy();
				if (a != b && !hasColided)
					if (Math.sqrt(Math.pow(bNext.x - aNext.x, 2) + Math.pow(bNext.y - aNext.y, 2)) <= aNext.radius + bNext.radius) {
						hasColided = true;

						Ball aOld = a.copy();
						Ball bOld = b.copy();
						a.bounce(bOld);
						b.bounce(aOld);

						// Cancel acceleration (no acceleration when boucning)
						accelerate(a, -deltaT);
						accelerate(b, -deltaT);
					}
			}
			// Move and accelerate the ball
			accelerate(b, deltaT);
			moveBall(b, deltaT);
		}
	}

	void accelerate(Ball b, double deltaT) {
		b.vx += deltaT * 0;
		b.vy += deltaT * -9.82;
	}

	void moveBall(Ball b, double deltaT) {
		b.x += deltaT * b.vx;
		b.y += deltaT * b.vy;
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
			double vectorScalar = (vel1.subtract(vel2)).dotProduct(pos1.subtract(pos2))
					/ Math.pow(pos2.subtract(pos1).length(), 2);

			Vector2 newVel = vel1.subtract(pos1.subtract(pos2).scale(massScalar * vectorScalar));
			vx = newVel.x;
			vy = newVel.y;
		}
	}

	public class Vector2 {
		protected double x, y;

		Vector2(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public Vector2 copy() {
			return new Vector2(x, y);
		}

		public double dotProduct(Vector2 vec) {
			return x * vec.x + y * vec.y;
		}

		public double length() {
			return Math.sqrt(x * x + y * y);
		}

		public Vector2 add(Vector2 vec) {
			return new Vector2(x + vec.x, y + vec.y);
		}

		public Vector2 subtract(Vector2 vec) {
			return new Vector2(x - vec.x, y - vec.y);
		}

		public Vector2 scale(double scalar) {
			return new Vector2(x * scalar, y * scalar);
		}

		public Vector2 rotate(Vector2 a, double d) {
			d = 2 * Math.PI - d;
			a.x = a.x * Math.cos(d) - a.y * Math.sin(d);
			a.y = a.x * Math.sin(d) + a.y * Math.cos(d);
			return a;
		}

		public Vector2 normalized() {
			return scale(1 / length());
		}
	}
}
