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

    plt.xlabel('Czas rozpoczęcia (ms)')
    plt.ylabel('Czas zakończenia (ms)')
    plt.title(f'Czasy przetwarzania dla liczby wątków = {thread}. Liczba zapytań = {len(df)}')
    plt.legend()
    plt.grid(True)

    plt.savefig(f'Data/Wykresy/query_times-{thread}.png')

def plot_execution_times(threads, overall_times):
    plt.figure(figsize=(10, 6))
    overall_times = [time / 1000 for time in overall_times]
    x_labels = threads
    x = list(range(len(x_labels)))

    plt.bar(x, overall_times, color='blue', alpha=0.7, width=0.8)

    plt.xticks(x, x_labels)

    plt.xlabel('Liczba wątków')
    plt.ylabel('Łączny czas wykonania (s)')
    plt.title('Łączny czas wykonania w zależności od liczby wątków')
    plt.grid(True, axis='y')

    plt.savefig('Data/Wykresy/overall_execution_times.png')

threads = [1,2, 4, 8, 16,32,64,128,1024]
overall_times = []
for thread in threads:
    plot_query_times(f'results-{thread}.csv', thread)
    overall_times.append(pd.read_csv(f'results-{thread}.csv', names=['start_time', 'end_time', 'duration', 'query_type'])['end_time'].max())
plot_execution_times(threads, overall_times)