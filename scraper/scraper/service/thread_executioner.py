# For threading
from __future__ import print_function

import threading
# For victim queue
import time
from queue import Queue
# For raising error
import ctypes

import inspect


class ThreadExecutioner:
    @staticmethod
    def mass_murder(victims: Queue):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        try:
            while True:
                victim = victims.get(block=True)
                print("killing: " + victim)
                ThreadExecutioner.murder(victim)
        except SystemExit:
            print()
            # TODO: logging

    @staticmethod
    def murder(victim: threading.Thread):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        alive = victim.is_alive()
        if alive:
            if not ctypes.pythonapi.PyThreadState_SetAsyncExc(victim, ctypes.py_object(SystemExit)):
                raise ChildProcessError
        victim.join()

        return alive

    @staticmethod
    def execute(target, tasks: Queue, *args):
        print("Function -> '{}'\t\t".format(inspect.currentframe().f_code.co_name) + " Thread -> " + str(
            threading.get_ident()))
        hit_queue = Queue()
        # Thread killer is for killing the parent or child thread once they run out of links.
        thread_killer = threading.Thread(target=ThreadExecutioner.mass_murder, args=[hit_queue], daemon=True)
        # This will not kill any threads until they are stalled
        thread_killer.start()

        try:
            while True:
                # Iterate through the links of the queue passed in and parse them using the target function passed in
                task = tasks.get(block=True)
                print(task)

                threading.Thread(target=target, args=(task, hit_queue, *args), daemon=True)

        except SystemExit:
            ThreadExecutioner.murder(thread_killer)
            print('Done scraping parent links')
