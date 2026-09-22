import numpy as np
import matplotlib.pyplot as plt
from sympy import symbols, cos, sin, diff, N
from matplotlib.animation import FuncAnimation
from scipy.optimize import fsolve

# Define the symbols
theta = symbols('theta')
r = 7 / 39.37 # Define the lever length (meters)
# x_offset = -2.625 / 39.37 # Define the x offset (meters)
axisheight = 13.995234 / 39.37 # 13.995234 inches to meters
# Function to calculate theta for a given distance
def calculate_theta(distance, initial_theta=0.1):
    # Camera position
    camera_x = r * cos(theta)
    camera_y = r * sin(theta) + axisheight

    angle_x = r * cos(theta)
    angle_y = r * sin(theta) + axisheight

    # Slope of the lever and the negative reciprocal for LoS
    slope_lever = (sin(theta)- axisheight) / cos(theta)
    slope_los = -1 / slope_lever

    # Function f(theta) for LoS to pass through (distance, 0)
    f_theta = 0 - camera_y - slope_los * (distance - camera_x)

    # Derivative of f(theta)
    f_prime_theta = diff(f_theta, theta)

    # Newton's method for finding theta
    theta_val = initial_theta
    tolerance = 1e-8
    max_iterations = 1000
    for _ in range(max_iterations):
        f_val = N(f_theta.subs(theta, theta_val))
        f_prime_val = N(f_prime_theta.subs(theta, theta_val))

        if abs(f_prime_val) < 1e-6:
            break

        theta_next = theta_val - f_val/f_prime_val

        if abs(theta_next - theta_val) < tolerance:
            break

        theta_val = theta_next % (2 * np.pi)

    return float(theta_val)

# Initial setup for the plot
fig, ax = plt.subplots(figsize=(8, 6))
ax.set_xlabel("distance (m)")
ax.set_ylabel("height (m)")
ax.set_title("System Visualization")
ax.axhline(0, color='black', linewidth=0.5)
ax.axvline(0, color='black', linewidth=0.5)
ax.grid(True)
ax.axis('equal')

# Lines and scatter points
angle_line = ax.plot([], [], label="Lever_r", color="orange")
lever_line, = ax.plot([], [], label="Lever", color="blue")
los_line, = ax.plot([], [], label="Line of Sight", linestyle="--", color="red")
camera_point, = ax.plot([], [], 'go', label="Camera")
target_point, = ax.plot([8], [0], 'o', color="orange", label="Target Point")

# Animation update function
def update(frame):
    max_distance = 8  # Maximum distance in meters
    min_distance = r + 0.1  # Minimum distance in meters
    center = (max_distance + min_distance) / 2  # Center of the oscillation
    range = center - min_distance  # Range of the oscillation
    distance = center + range * np.sin(frame / 10)  # Oscillate distance around 4 meters
    theta_val_deg = calculate_theta(distance) * 180 / np.pi

    if 33 <= theta_val_deg <= 77:
        lever_color = "green"  # Inside specified range
        camera_color = "green"
    else:
        lever_color = "blue"  # Outside specified range
        camera_color = "red"
    
    # Update camera and lever coordinates
    theta_val = np.radians(theta_val_deg)
    camera_x = r * np.cos(theta_val)
    angle_x = r * np.cos(theta_val)
    angle_y = r * np.sin(theta_val) + axisheight
    camera_y = r * np.sin(theta_val) + axisheight
    lever_x = camera_x
    lever_y = camera_y

    # LoS direction flipped
    los_x = np.sin(theta_val)
    los_y = -np.cos(theta_val)

    # Update lines and points with the new color based on theta range
    angle_line.set_data([0, angle_x], [axisheight, angle_y])
    lever_line.set_data([0, lever_x], [axisheight, lever_y])
    lever_line.set_color(lever_color)
    los_line.set_data([camera_x, camera_x + los_x*10], [camera_y, camera_y + los_y*10])
    camera_point.set_data([camera_x], [camera_y])
    camera_point.set_color(camera_color)
    target_point.set_data([distance], [0])

    return lever_line, los_line, camera_point, target_point


# Creating the animation
ani = FuncAnimation(fig, update, frames=np.arange(0, 200, 1), blit=True)
plt.legend()

# Plot theta as a function of distance
max_distance = 8  # Maximum distance in meters
num_samples = 2000  # Number of samples

# Generate distances using linspace
distances = np.linspace(r + 0.1, max_distance, num_samples)

# Arrays to store x and y data
x_data = []
y_data = []

# Calculate theta for each distance and store in arrays
for distance in distances:
    theta_val = calculate_theta(distance) * 180 / np.pi
    x_data.append(distance)
    if (distance == r):
        y_data.append(0)
    else:
        y_data.append(theta_val)

# Perform a cubic fit
coefficients = np.polyfit(x_data, y_data, 5)

# Create a polynomial function with the obtained coefficients
cubic_fit = np.poly1d(coefficients)

# Generate y values using the cubic fit function
fitted_y = cubic_fit(x_data)

# Define the target y values in degrees and convert them to radians
y_degrees = [30, 75]

# Define a function to find the roots of
def find_roots(x, y_target):
    return cubic_fit(x) - y_target

# Solve for x for each y value
x_roots = []
for y in y_degrees:
    # Estimate an initial guess for x
    initial_guess = 30
    root = fsolve(find_roots, initial_guess, args=(y))
    x_roots.append(root[0])

# Print the solutions
for i, y_deg in enumerate(y_degrees):
    print(f"x value for y = {y_deg} degrees: {x_roots[i]}")
print('Fitted Curve $y = %.2f x^5 %.2f x^4 %.2f x^3 + %.2f x^2 + %.2f x + %.2f$' % (coefficients[0], coefficients[1], coefficients[2], coefficients[3], coefficients[4], coefficients[5]))
error = fitted_y - y_data
max_error = np.max(error)
min_error = np.min(error)
print("Max error: ", max_error)
print("Min error: ", min_error)
# Plotting the original data and the fitted curve
plt.figure(figsize=(8, 6))
plt.plot(x_data, y_data, '-r', label='Original Data')
plt.plot(x_data, fitted_y, '-b', label='Fitted Curve')
plt.plot(x_data, error, '-k', label='error')
plt.xlabel('Distance (m)')
plt.ylabel('Theta (degrees)')
plt.title('Theta vs Distance')
plt.legend()
plt.grid(True)
plt.show()


