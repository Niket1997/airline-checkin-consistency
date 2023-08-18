## Concurrency Problem Solved - Slow

In this approach, all the transaction fire search query on the
database around the same time. When a thread acquires lock for a row, other threads keep 
waiting until the lock is released. Once the lock is released by the thread (after commit), 
the remaining threads read the same row & find that the user_id is not null for that row. 
So they move to the next row where the userId is null. Again, on of the threads acquire the lock
and others keep waiting. Here, we make sure that all the seats are booked only once, but 
this process is extremely slow. 