import matplotlib.pyplot as plt

# Data file path
file_path = 'philosophers_logs_phil.txt'  # Replace with your actual file path

# Initialize lists to store the data
philosopher_counts_asym = [2, 5, 10, 30, 50, 100, 300]
philosopher_counts_conduct = [2, 5, 7, 9]
asymmetric_fork_times = [1488, 1825, 1780, 2237, 2670, 2279, 2135]
conductor_fork_times = [250, 5567, 9099, 13589]
conductor_table_times = [1633, 2290,3101, 1489]
conductor_times = [i + j for i, j in zip(conductor_fork_times, conductor_table_times)]

# Plotting
plt.figure(figsize=(12, 8))

print(asymmetric_fork_times)
# Asymmetric Method
plt.plot(philosopher_counts_asym, asymmetric_fork_times, label='Asymmetric - Fork Wait Time', marker='o')

# Conductor Method
plt.plot(philosopher_counts_conduct, conductor_fork_times, label='Conductor - Fork Wait Time', marker='^')
plt.plot(philosopher_counts_conduct, conductor_table_times, label='Conductor - Table Wait Time', marker='x')
plt.plot(philosopher_counts_conduct, conductor_times, label='Conductor - Table Wait Time', marker='x')

# Adding labels and title
plt.xlabel('Number of Philosophers')
plt.ylabel('Average Wait Time (ms)')
plt.title('Average Wait Times for Philosophers (Asymmetric vs Conductor Methods)')
plt.legend()

# Show the plot
plt.grid(True)
plt.tight_layout()
plt.show()

