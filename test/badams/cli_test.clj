(ns badams.cli-test
  (:require [badams.cli :as sut]
            [badams.core :as core]
            [badams.repository :as repo]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(deftest show-report!-test
  (let [repo (repo/atomic-repo [{:line 1 :DateOfBirth (core/date "1/2/1970")}
                                {:line 2 :DateOfBirth (core/date "1/3/1980")}])]

    (is (=
"|	TEST:
|
| line | DateOfBirth |
|------+-------------|
|    1 |    1/2/1970 |
|    2 |    1/3/1980 |"

         (str/trim (with-out-str (sut/show-report! repo [repo/unsorted "TEST"])))))))

(deftest populate-repo!-test
  (is (= [{:FirstName "FN"
           :LastName "LN"
           :Email "EM"
           :FavoriteColor "BLUE"
           :DateOfBirth (core/date "1/1/1970")}]
         (repo/values (sut/populate-repo! ["./test/resources/cli_test.csv"])))))

(deftest -main-test
  (testing "main outputs all of the reports"
    (is (= (slurp "./test/resources/cli_main_test.expected")
           (with-out-str
             (sut/-main "./test/resources/cli_main_test.csv"))))))
