# OOP_Ex2

This project is divided into two parts:
#### Part 1 - Comparison of running times of opening files using 3 methods
#### Part 2 - Added an option to prioritize tasks using the threadpool

## Part 1
In this part, we will create text files with a random number of lines in each file (using the random class) and then we will want to get the total number of lines of all files together, we will do this using 3 different methods and compare their running times

 - Normal counting, file by file
 - Using threads
 - Using a threadpool

About the running times, we found that using threads is the fastest, followed by using a threadpool and finally normal counting is the slowest
Although logically threadpool should be the fastest,  sometimes the process of creating a threadpool slows down the process and depends on the computer and the file system, therefore it makes sense that we will accept that a thread will be faster, in addition, we defined the amount of thread in the threadpool as the number of files so that the process of creating the threads was not spared and was the same between the threads and the threadpool , and therefore there is no reason to expect better results when using threadpool

We will see illustrations of the resulting running times:
- Counting 100 files:
<img width="248" alt="image" src="https://user-images.githubusercontent.com/98847692/211792565-7d97b86e-04f7-4b80-a8f8-07c3b3cb33ea.png">

- Counting 1000 files:
<img width="251" alt="image" src="https://user-images.githubusercontent.com/98847692/211793378-4b93328b-aab1-4234-9fbd-c18bd08c0e9b.png">


- Counting 10000 files:
<img width="259" alt="image" src="https://user-images.githubusercontent.com/98847692/211803897-1ab6b388-ac43-4209-936d-f636c4634065.png">






## Part 2
In this project, we would like to overcome the problem created by using a threadpool or by giving a callable<T> to a thread, java allows giving priority to a certain thread over the rest, but a callable<T> type task cannot be performed using a normal thread and therefore it cannot be prioritized.
Also, when using a threadpool by providing a collection of tasks (of runnable or callable type) it is not possible to give priority to a certain thread over the rest (and there is no way to know which thread will receive which task)

Therefore, we would like to create a new task type that will contain a priority, and this priority will determine which task will be sent to be executed first.

To carry out this project, we have implemented a class that inherits threadpool, which contains callable tasks with the addition of a priority for each task. When we activate threadpool, it will receive task priorities in the queue, which will be arranged according to their priority, and in that order, they will be sent for execution. In the threadpool or callable we can set a priority for which task will be executed first




#### Using design patterns:
- Factory 
In creating the task class, we used a Factory design pattern (defining the constructor as private and creating a static method for building objects that calls from the class to the constructor)

- Adapter
To be able to know at any given moment what the maximum priority is, we would like to monitor a function that is activated when a thread becomes free and a new task enters execution, and every time this function is activated to update what the maximum priority is currently in the queue, however, to do this we need to convert our task to the FutureTask type and therefore We had to use an Adapter design template that serves as an adapter between our Task and the FutureTask that is sent as a parameter to the execute function

## Thanks for reading!
