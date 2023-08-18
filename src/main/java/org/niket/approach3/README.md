## Concurrency Problem

In this approach, all the transaction fire search query on the 
database around the same time. Many threads first read that the row is available &
do the update. However, many threads read the same row & update it resulting in few seats 
getting assigned among users. 