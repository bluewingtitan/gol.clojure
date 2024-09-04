(ns gol.core
  (:gen-class)
  (:require [clojure.math :as math]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def test-gamestate '[[0 0 0 0 0 0 0 0]
                      [0 0 0 0 0 0 0 0]
                      [0 0 0 0 0 0 0 0]
                      [0 0 0 1 1 0 0 0]
                      [0 0 0 1 1 0 0 0]
                      [0 0 0 0 0 0 0 0]
                      [0 0 0 0 0 0 0 0]
                      [0 0 0 0 0 0 0 0]])

(def blinker-gamestate '[[0 0 0 0 0]
                         [0 0 1 0 0]
                         [0 0 1 0 0]
                         [0 0 1 0 0]
                         [0 0 0 0 0]])

(def glider-gamestate '[[0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 1 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 1 0 0 0 0 0 0 0 0 0 0 0]
                        [0 1 1 1 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]
                        [0 0 0 0 0 0 0 0 0 0 0 0 0 0 0]])

(defn get-symbol [state]
  (if (= state 1) "☑" "☐"))

(defn get-line [line]
  (reduce str (map get-symbol line)))

(defn print-game [game]
  (println (reduce str (map (fn [row]
                              (str (get-line row) "\n")) game))))

(def neighbours
  [[-1 1] [0 1] [1 1]
   [-1 0] [1 0]
   [-1 -1] [0 -1] [1 -1]])

(defn in-bounds "checks if value is between min (inclusive) and max (exclusive)"
  [min max val]
  (and (>= val min) (< val max)))

(defn get-cell-or-default [state x y default]
  (let [size (count state)
        x-valid (in-bounds 0 size x)
        y-valid (in-bounds 0 size y)]
    (if (and x-valid y-valid)
      (get (get state x) y)
      default)))

(defn get-neighbor-cells [x y]
  (map
   (fn [neighbour]
     [(+ (get neighbour 0) x) (+ (get neighbour 1) y)]) neighbours))

(defn get-neighbor-count [x y state]
  (reduce + (map
             (fn [coords] (get-cell-or-default state (get coords 0) (get coords 1) 0))
             (get-neighbor-cells x y))))

(defn get-new-state "returns whether a cell is alive given it's current state and alive neighbour count"
  [is-alive neighbour-count]
  (if is-alive
    ; case: cell is alive => continues with 2 and 3 neighbours
    (if (and (>= neighbour-count 2) (< neighbour-count 4)) 1 0)
    ; case: cell is dead => becomes alive with exactly 3 neighbours
    (if (= neighbour-count 3) 1 0)))


(defn do-evolution "receive a gamestate as 2d array, calculate the next gamestate"
  [state]
  (into []
        (map-indexed (fn [x row]
                       (into [] (map-indexed
                                 (fn [y cell]
                                   (get-new-state (= cell 1) (get-neighbor-count x y state))) row))) state)))


(defn evolute-x-times
  ([state i] (evolute-x-times state i identity))
  ([state i interceptor]
   (if (> i 0)
     (let [next-state (do-evolution state)]
       (interceptor next-state)
       (evolute-x-times next-state (- i 1) interceptor))
     true)))

(evolute-x-times glider-gamestate 48 print-game)
