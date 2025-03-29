import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

def plot_query_times(csv_file, thread):
    # Read CSV file
    df = pd.read_csv(csv_file, names=['start_time', 'end_time', 'duration', 'query_type'])

    # Sort by start time to visualize better
    df = df.sort_values(by='start_time')

    # Define colors for different query types
    colors = {'DECRYPTED': 'blue', 'ENCRYPTED': 'red'}
    df['color'] = df['query_type'].map(colors).fillna('gray')

    plt.figure(figsize=(10, 6))

    # Scatter plot for start and end times
    for query_type, color in colors.items():
        subset = df[df['query_type'] == query_type]
        plt.scatter(subset['start_time'], subset['end_time'], color=color, label=query_type, alpha=0.6)

    # Trend line
    sns.regplot(x=df['start_time'], y=df['end_time'], scatter=False, color='black', line_kws={'linestyle':'dashed'})

    plt.xlabel('Start Time (ms)')
    plt.ylabel('End Time (ms)')
    plt.title(f'Query Execution Threads = {thread} N = {len(df)}')
    plt.legend()
    plt.grid(True)

    plt.savefig(f'query_times-{thread}.png')

threads = [2, 4, 8, 16]
for thread in threads:
    plot_query_times(f'results-{thread}.csv', thread)