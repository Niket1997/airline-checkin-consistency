## Concurrency Problem Solved - Fast

In this approach, all the transaction fire search query on the
database around the same time. When a thread acquires lock for a row, other
threads skip this locked row & move to the next available row.
Here, we make sure that all the seats are booked only once, & the process is faster.