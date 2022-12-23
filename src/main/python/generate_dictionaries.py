import requests
import copy
from typing import Set, Dict

# Source dictionaries:
# http://czterycztery.pl/slowo/lista_frekwencyjna_z_odmianami/
# https://sjp.pl/sl/growe/?d=6

BASE_PATH = '../resources/dictionaries/'

def get_words_from_sjp() -> Set[str]:
  words = set()
  url = 'https://sjp.pl/sl/growe/?d=6'
  response = requests.get(url)
  html = response.text
  for line in html.splitlines():
    if line.startswith('<a target="_blank" href="'):
      index = line.index('>') + 1
      words.add(line[index:index+6].upper())
  return words

def get_words_from_czterycztery() -> Set[str]:
  # TODO: load words from actual czterycztery.pl website
  words = set()
  with open(BASE_PATH + 'czterycztery-words.txt') as f:
    for line in f.read().splitlines():
      if len(line) == 6:
        words.add(line.upper())
  return words

def save_words_to_file(filename: str, words: Set[str]):
  f = open(filename, 'w')
  for word in words:
    f.write(word + '\n')
  f.close()

def add_verbs(words: Set[str], accepted_words: Set[str]):
  for word in words:
    if word[5] == 'Ć':
      accepted_words.add(word)

def remove_plural_nouns(words: Set[str]):
  for word in list(words):
    if word[-1] == 'Y':
      words.remove(word)

def main():
  words1 = get_words_from_sjp()
  words2 = get_words_from_czterycztery()
  words = set.union(words1, words2)

  # TODO: make words that definitely could not be accepted (eg. ending with "y")
#  possible_words = copy.deepcopy(words)
  possible_words = words1.intersection(words2)
  remove_plural_nouns(possible_words)

  tmp = list(possible_words)
  tmp.sort()
  print(tmp)
  print(len(tmp))

  accepted_words = set()
  add_verbs(possible_words, accepted_words)
  possible_words = possible_words - accepted_words
  print(possible_words)
  
  save_words_to_file(BASE_PATH + 'words-to-enter.txt', words)
  save_words_to_file(BASE_PATH + 'words-to-guess.txt', accepted_words)


if __name__ == "__main__":
  main()
