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
		// balls[0] = new Ball(width / 2, height * 0.5, 0.6, -1.2, 0.2);
		// balls[1] = new Ball(width / 2, height * 0.2, -0.3, 2.4, 0.3);
		balls[0] = new Ball(width / 2, height * 0.5, 0, 0, 0.2);
		balls[1] = new Ball(width / 2 + 1, height * 0.2, 0, 0, 0.3);
	}

	void step(double deltaT) {
		boolean hasColided = false;
		// TODO this method implements one step of simulation with a step deltaT
		for (Ball b : balls) {
			double acc = -1.82;
			double offset = 0.03;
			// Detect collision with the border, the next frame
			Ball ballNextStep = b.copy();
			ballNextStep.vx += deltaT * 0;
			ballNextStep.vy += deltaT * acc;
			ballNextStep.x += deltaT * b.vx;
			ballNextStep.y += deltaT * b.vy;
			if (ballNextStep.x < ballNextStep.radius || ballNextStep.x > areaWidth - ballNextStep.radius) {
				b.vx *= -1; // change direction of ball
				b.vy -= deltaT * (acc);
				// b.x += offset * Math.signum(b.vx);
				continue;
			}
			if (ballNextStep.y < ballNextStep.radius || ballNextStep.y > areaHeight - ballNextStep.radius) {
				b.vy *= -1;
				b.vy -= deltaT * acc;
				// b.y += offset * Math.signum(b.vy);
				continue;
			}

			// compute new position according to the speed of the ball

			// Check if collision
			for (Ball a : balls) {
				if (a != b && !hasColided)
					if (Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)) <= a.radius + b.radius) {
						hasColided = true;

						Ball olda = a.copy();
						Ball oldb = b.copy();
						a.bounce(oldb);
						b.bounce(olda);

						Vector2 midle = new Vector2(a.x - b.x, a.y - b.y);
						// double d = lefAngel(midle,new Vector2(0,1));
						// rotateBall(a,d);
						// rotateBall(b,d);
						// a.vy *= -1;
						// b.vy *= -1;
						// rotateBall(a,inv(d));
						// rotateBall(b,inv(d));

						a.x += offset * midle.x / midle.length();
						a.y += offset * midle.y / midle.length();
						b.x -= offset * midle.x / midle.length();
						b.y -= offset * midle.y / midle.length();
					}
			}
			b.vx += deltaT * 0;
			b.vy += deltaT * acc;
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
		}
	}

	void offsettAndCompensate(Ball b, double offset, Vector2 v, double acc) {
		b.x += offset * v.x;
		b.y += offset * v.y;

		double timeStep = v.y * offset / b.vy;
		b.vy -= timeStep * acc;

	}

	double lefAngel(Vector2 b, Vector2 a) {
		Vector2 zero = new Vector2(1, 0);
		double aDeg = angeBetweenVectors(zero, a);
		double bDeg = angeBetweenVectors(zero, b);

		int aSign = (int) Math.signum(a.y);
		int bSign = (int) Math.signum(b.y);

		if (a.y == 0) {
			aSign = 1;
		}
		if (b.y == 0) {
			bSign = 1;
		}

		if (aSign == 1 && bSign == 1) {
			if (aDeg > bDeg) {
				return angeBetweenVectors(a, b);
			} else {
				return inv(angeBetweenVectors(a, b));
			}

		} else if (aSign == 1 && bSign == -1) {
			if (aDeg + bDeg > Math.PI) {
				return inv(angeBetweenVectors(a, b));
			} else {
				return angeBetweenVectors(a, b);
			}

		} else if (aSign == -1 && bSign == 1) {
			if (aDeg + bDeg > Math.PI) {
				return angeBetweenVectors(a, b);
			} else {
				return inv(angeBetweenVectors(a, b));
			}
		} else {
			if (aDeg > bDeg) {
				return inv(angeBetweenVectors(a, b));
			} else {
				return angeBetweenVectors(a, b);
			}
		}

	}

	double inv(double d) {
		return Math.PI * 2 - d;
	}

	double angeBetweenVectors(Vector2 a, Vector2 b) {
		return Math.acos((a.x * b.x + a.y * b.y) / (a.length() * b.length()));
	}

	void rotateBall(Ball a, double d) {
		double oldVx = a.vx;
		double oldVy = a.vy;

		a.vx = Math.cos(d) * oldVx - Math.sin(d) * oldVy;
		a.vy = Math.sin(d) * oldVx + Math.cos(d) * oldVy;
	}

	Vector2 rotate(Vector2 a, double d) {
		double oldVx = a.x;
		double oldVy = a.y;

		a.x = Math.cos(d) * oldVx - Math.sin(d) * oldVy;
		a.y = Math.sin(d) * oldVx + Math.cos(d) * oldVy;
		return a;
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
			Vector2 base1 = diffVec.scale(1 / diffVec.length());
			Vector2 base2 = new Vector2(x, y);
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