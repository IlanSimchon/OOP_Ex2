# OOP_Ex2

This prefect is divided into two parts:
### Part 1 - Comparison of running times of opening files using 3 methods
### Part 2 - Added an option to prioritize tasks using the threadpool

## Part 1


## Part 2
In this project, we would like to overcome the problem created by using a threadpool or by giving a callable<T> to a thread, java allows giving priority to a certain thread over the rest, but a callable<T> type task cannot be performed using a normal thread and therefore it cannot be prioritized.
Also, when using a threadpool by providing a collection of tasks (of runnable or callable type) it is not possible to give priority to a certain thread over the rest (and there is no way to know which thread will receive which task)

Therefore, we would like to create a new task type that will contain a priority, and this priority will determine which task will be sent to be executed first.
