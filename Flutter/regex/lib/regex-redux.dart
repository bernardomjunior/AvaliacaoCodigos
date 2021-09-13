/* The Computer Language Benchmarks Game
   https://salsa.debian.org/benchmarksgame-team/benchmarksgame/

   contributed by Isaac Gouy, RegExp from Jos Hirth's program
*/

import 'dart:async';
import 'dart:io';
import 'dart:isolate';

import 'package:regex/Repository.dart';

void run(String text) {
  final otherIsolate = Completer<SendPort>();
  ready() => otherIsolate.future;
  final mainIsolate = ReceivePort();
  Isolate.spawn(requestReply, mainIsolate.sendPort);

  var initial = 0, cleaned = 0;

  mainIsolate.listen((dynamic message) {
    if (message is SendPort) {
      otherIsolate.complete(message);
    } else {
      print('\n$initial\n$cleaned\n${message as int}');
      mainIsolate.close();
      endCode();
    }
  });
  final z = (text.replaceAll(RegExp('>.*\n|\n', multiLine: true), ''));

  ready().then((SendPort p) {
    p.send(z);
    printPatternMatches(z);
    initial = text.length;
    cleaned = z.length;
  });

}

void printPatternMatches(String s) {
  const simple = [
    'agggtaaa|tttaccct',
    '[cgt]gggtaaa|tttaccc[acg]',
    'a[act]ggtaaa|tttacc[agt]t',
    'ag[act]gtaaa|tttac[agt]ct',
    'agg[act]taaa|ttta[agt]cct',
    'aggg[acg]aaa|ttt[cgt]ccct',
    'agggt[cgt]aa|tt[acg]accct',
    'agggta[cgt]a|t[acg]taccct',
    'agggtaa[cgt]|[acg]ttaccct'
  ];

  simple.forEach((each) {
    final regex = RegExp(each, multiLine: true);
    print('$each ${regex.allMatches(s).length}');
  });
}

int magic(String s) {
  const magic = [
    ['tHa[Nt]', '<4>'],
    ['aND|caN|Ha[DS]|WaS', '<3>'],
    ['a[NSt]|BY', '<2>'],
    ['<[^>]*>', '|'],
    ['\\|[^|][^|]*\\|', '-'],
  ];

  magic.forEach((each) {
    final regex = RegExp(each.first, multiLine: true);
    s = s.replaceAll(regex, each.last);
  });
  return s.length;
}

void requestReply(SendPort p) {
  ReceivePort port = ReceivePort();
  p.send(port.sendPort);
  port.listen((dynamic s) {
    if (s is String) {
      p.send(magic(s));
    }
  });
}
