/* The Computer Language Benchmarks Game
   https://salsa.debian.org/benchmarksgame-team/benchmarksgame/

   contributed by Isaac Gouy, borrowing from Andrey Filatkin's program
*/

import 'dart:async';
import 'dart:io';
import 'dart:isolate';
import 'dart:math';
import 'dart:typed_data';

late Int32List fact; // each isolate has a factorials Int32List

void setFactorials(int n) {
  fact = Int32List(n + 1);
  fact[0] = 1;
  for (int i = 1; i <= n; i++) {
    fact[i] = fact[i - 1] * i;
  }
}

void fannkuch(Message m) {
  final n = m.n;
  final start = m.start;
  final end = m.end;

  final p = Int32List(n);
  final pp = Int32List(n);
  final count = Int32List(n);

  // first permutation
  for (var i = 0; i < n; i++) {
    p[i] = i;
  }
  int idx = start;
  for (var i = n - 1; i > 0; i--) {
    final d = div(idx, fact[i]);
    count[i] = d;
    idx = idx % fact[i];

    for (var j = 0; j < n; j++) {
      pp[j] = p[j];
    }

    for (var j = 0; j <= i; j++) {
      if (j + d <= i) {
        p[j] = pp[j + d];
      } else {
        p[j] = pp[j + d - i - 1];
      }
    }
  }

  var maxFlips = 1;
  var checkSum = 0;

  idx = start;
  for (var sign = true;; sign = !sign) {
    var first = p[0];
    if (first != 0) {
      var flips = 1;
      if (p[first] != 0) {
        for (var j = 1; j < n; j++) {
          pp[j] = p[j];
        }
        var p0 = first;
        while (true) {
          flips++;
          var i = 1;
          var j = p0 - 1;
          while (i < j) {
            final t = pp[i];
            pp[i] = pp[j];
            pp[j] = t;
            i++;
            j--;
          }
          final t = pp[p0];
          pp[p0] = p0;
          p0 = t;
          if (pp[p0] == 0) {
            break;
          }
        }
      }

      if (maxFlips < flips) {
        maxFlips = flips;
      }
      if (sign) {
        checkSum += flips;
      } else {
        checkSum -= flips;
      }
    }

    idx++;
    if (idx == end) {
      break;
    }

    // next permutation
    if (sign) {
      p[0] = p[1];
      p[1] = first;
    } else {
      final t = p[1];
      p[1] = p[2];
      p[2] = t;
      for (var k = 2;; k++) {
        count[k]++;
        if (count[k] <= k) {
          break;
        }
        count[k] = 0;
        for (var j = 0; j <= k; j++) {
          p[j] = p[j + 1];
        }
        p[k + 1] = first;
        first = p[0];
      }
    }
  }
  m.setReply(checkSum, maxFlips);
}

int div(int val, int by) {
  return (val - val % by) ~/ by;
}

final ports = <SendPort>[];
final requests = <Message>[];
ReceivePort mainIsolate = ReceivePort();

void main() async {
  final n = 12;
  await spawnIsolates();
  setRequests(n);
  var awaitedReplies = requests.length; // each should receive a reply

  // send an initial request to each spawned isolate
  ports.forEach((p) {
    if (requests.length > 0) {
      p.send(n); // setFactorials for each spawned isolate
      p.send(requests.removeLast());
    }
  });
  var checkSum = 0, maxFlips = 0;

  mainIsolate.listen((dynamic m) {
    if (m is Message) {
      // accumulate partial results
      checkSum += m.sum;
      if (m.flips > maxFlips) {
        maxFlips = m.flips;
      }
      if (requests.length > 0) {
        // send another request to the isolate that finished
        m.replyTo.send(requests.removeLast());
      }
      if (--awaitedReplies < 1) {
        print("$checkSum\nPfannkuchen($n) = $maxFlips");
        mainIsolate.close();
      }
    }
  });
}

void setRequests(int n) {
  setFactorials(n);
  const pieces = 720;
  var size = div((fact[n] + pieces - 1), pieces);
  size += size % 2;
  final len = div((fact[n] + size - 1), size);
  for (var i = 0; i < len; i++) {
    final start = size * i;
    final end = min(fact[n], start + size);
    requests.add(Message(n, start, end));
  }
}

Future<void> spawnIsolates() async {
  ReceivePort replyPort = ReceivePort();
  final completers = <Completer>[];
  final barrier = <Future>[];
  var i = Platform.numberOfProcessors;
  while (i-- > 0) {
    final c = Completer<dynamic>();
    completers.add(c);
    barrier.add(c.future);
    Isolate.spawn(requestReply, replyPort.sendPort);
  }
  replyPort.listen((dynamic p) {
    ports.add(p as SendPort);
    completers.removeLast().complete();
  });
  await Future.wait<void>(barrier);
  replyPort.close();
}

void requestReply(SendPort p) {
  ReceivePort requestPort = ReceivePort();
  p.send(requestPort.sendPort);
  requestPort.listen((dynamic m) {
    if (m is int) {
      setFactorials(m); // factorials for this isolate
    } else if (m is Message) {
      // update and send the same message back
      p = m.replyTo;
      fannkuch(m);
      m.replyTo = requestPort.sendPort;
      p.send(m);
    }
  });
}

class Message {
  int n = 0, start = 0, end = 0;
  SendPort replyTo = mainIsolate.sendPort;
  Message(this.n, this.start, this.end);
  int get sum => start;
  int get flips => end;
  void setReply(int sum, int flips) {
    start = sum;
    end = flips;
  }
}
