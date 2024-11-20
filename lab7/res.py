import matplotlib.pyplot as plt
import numpy as np

# Read the parsed data
parsed_file_path = 'parsed_results.txt'

# Data containers
iterations = [1, 2, 5, 10, 20]  # The iterations based on your problem statement
asym_fork_wait_times = []
conductor_table_wait_times = []
conductor_fork_wait_times = []
conductor_total_wait_times = []

# Parse the file
with open(parsed_file_path, 'r') as file:
    lines = file.readlines()

    # We will accumulate wait times for each method
    current_iter = 1
    asym_fork_wait_list = []
    conductor_table_wait_list = []
    conductor_fork_wait_list = []
    conductor_total_wait_list = []

    for line in lines[1:]:  # Skip the header
        parts = line.strip().split(',')
        philosopher = int(parts[0])
        method = parts[1]
        fork_wait_time = int(parts[2])
        table_wait_time = int(parts[3]) if parts[3] else 0
        total_wait_time = int(parts[4]) if parts[4] else 0
        
        # Collect data for Asymmetric method
        if method == 'Asymmetric':
            asym_fork_wait_list.append(fork_wait_time)
        
        # Collect data for Conductor method
        elif method == 'Conductor':
            conductor_table_wait_list.append(table_wait_time)
            conductor_fork_wait_list.append(fork_wait_time)
            conductor_total_wait_list.append(total_wait_time)
        
        # Check if we reached the end of one iteration and update means
        if philosopher == 4:  # As you are testing with 5 philosophers, 4 is the last one
            if method == 'Asymmetric':
                asym_fork_wait_times.append(np.mean(asym_fork_wait_list))
                asym_fork_wait_list.clear()  # Clear list for next iteration
            elif method == 'Conductor':
                conductor_table_wait_times.append(np.mean(conductor_table_wait_list))
                conductor_fork_wait_times.append(np.mean(conductor_fork_wait_list))
                conductor_total_wait_times.append(np.mean(conductor_total_wait_list))
                # Clear lists for next iteration
                conductor_table_wait_list.clear()
                conductor_fork_wait_list.clear()
                conductor_total_wait_list.clear()

# Now we have the mean waiting times for each iteration in the corresponding lists

# Plotting the chart
plt.figure(figsize=(10, 6))

# Plot for Asymmetric method - Mean Fork Wait Time
plt.plot(iterations, asym_fork_wait_times, label='Asymmetric - Mean Fork Wait Time', marker='o')

# Plot for Conductor method
plt.plot(iterations, conductor_table_wait_times, label='Conductor - Mean Table Wait Time', marker='s')
plt.plot(iterations, conductor_fork_wait_times, label='Conductor - Mean Fork Wait Time', marker='^')
plt.plot(iterations, conductor_total_wait_times, label='Conductor - Total Wait Time', marker='x')

# Adding labels and title
plt.xlabel('Iterations')
plt.ylabel('Mean Waiting Time (ms)')
plt.title('Mean Waiting Times for Philosophers (Asymmetric vs Conductor Methods)')
plt.legend()

# Show the plot
plt.grid(True)
plt.tight_layout()
plt.show()
