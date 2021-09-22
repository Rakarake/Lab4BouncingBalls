all: bouncing_balls/*.java
	javac bouncing_balls/Animator.java bouncing_balls/Model.java
run:
	java bouncing_balls.Animator
clean:
	rm bouncing_balls/*.class
