# Need a heap ofc
from __future__ import print_function

import heapq
# For release timers
import threading
# For the release queue
from queue import Queue

import inspect


class ReleaseHeap(object):
    """
        elem_list must be a list of mutable tuples!
        the left value needs to be comparable and incrementable/decrementable
    """

    def __init__(self, elem_list: list, time_sec: int):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        # init the heap and queue and assign variables
        heapq.heapify(elem_list)
        self.heap = elem_list
        self.time_sec = time_sec
        self.release_queue = Queue()

        # quick access to pair by it's value
        self.hash_map = dict([(pair[1], pair) for pair in self.heap])

    def __iter__(self):
        return self.heap

    def __next__(self):
        return self.heap[0][1]

    def capture(self):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        # pop off the top of the heap and increment the first value of the pair
        pair = heapq.heappop(self.heap)
        pair[0] += 1
        heapq.heappush(self.heap, pair)

        # put the value into the queue
        self.release_queue.put(pair[1])

        # start the timer for release
        threading.Timer(self.time_sec, self.release).start()

        return pair[0]

    def release(self):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        # get the id of the next to be released and with that the pair
        top_value = self.release_queue.get()
        pair = self.hash_map[top_value]

        # beginning value can be non-zero and whatever you choose should be returned to in the end
        pair[0] -= 1


0
