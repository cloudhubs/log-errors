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
        print("my name is '{}'\t".format(inspect.currentframe().f_code.co_name) + str(threading.get_ident()))
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
        print("my name is '{}'\t".format(inspect.currentframe().f_code.co_name) + str(threading.get_ident()))
        alive = victim.is_alive()
        if alive:
            if not ctypes.pythonapi.PyThreadState_SetAsyncExc(victim, ctypes.py_object(SystemExit)):
                raise ChildProcessError
        victim.join()

        return alive

    @staticmethod
    def execute(target, tasks: Queue, *args):
        print("my name is '{}'\t".format(inspect.currentframe().f_code.co_name) + str(threading.get_ident()))
        hit_queue = Queue()
        thread_killer = threading.Thread(target=ThreadExecutioner.mass_murder, args=[hit_queue], daemon=True)
        # thread_killer.start()

        try:
            while True:
                if not isinstance(tasks, Queue):
                    print(str(threading.get_ident()) + ": " + str(tasks))
                    # time.sleep(5)
                else:
                    task = tasks.get(block=True)
                    print(task)

                    threading.Thread(target=target, args=(task, hit_queue, *args), daemon=True)

        except SystemExit:
            ThreadExecutioner.murder(thread_killer)
            print('Done scraping parent links')
