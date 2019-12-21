# CalendarApp

This project counts the meetings found in a csv file. It provides two implementations:

## Minimum Viable Product

### Input
This implementation can only handle basic input. i.e. in the format `beginDate, endDate, dayOfTheWeek` where beginDate and endDate are in the format "2018-02-02" and dayOfTheWeek is a string representing the day of the week such as "Monday".

### Algorithm
This implementation stores each interval as a MeetingInterval object. Then, it sums all of their meeting totals and returns the result.

## Full Product

### Input
This implementation can handle both overlapping input and vacations/holidays. So, the format is read the same way as in the MVP only now the input can also be in the format `beginDate, endDate, ["Vacation" | "Holiday"]`.

### Algorithm
This implementation utilizes an interval tree to store each interval. It is based off of a red-black tree to make sure that operations on the tree are fast.

A list of meetingIntervals and vacations (also stored as meetingIntervals) are obtained from the csv. Then, the meetingIntervals are inserted in the tree where they are merged if they overlap with each other. After they are inserted into the tree, the vacation intervals are removed from the tree.

#### Insertion details
Insertion into the tree keeps track of...
1. The max date of its children
2. A mapping of the max date for each weekday of its children

This is necessary for the "find overlapping" algorithm of the interval tree. The first is needed in the case that we are looking for any overlapping intervals and the second is needed if we are looking for the overlapping intervals of a specific day. This first case is used when removing vacations and the second is used when merging meetings that overlap.
On a rotation, these values are updated up the tree.

#### Deletion details
To remove meetings that overlap with vacations, the algorithm first finds all overlapping intervals. Then for each overlapping interval, it splits it up based on the vacation. On a timeline this would mean it either removes it entirely (if it covers all of the date), it removes a portion of it, or it splits it into two different intervals. Regardless, after the split it inserts the resulting interval(s) back into the tree if there are any.

### Running Times

Since this is based on a Red-Black tree, running time for insertion, deletion, and search are all O(logN).
