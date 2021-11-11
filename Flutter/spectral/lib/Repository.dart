import 'package:http/http.dart' as http;

const BASE_URL = "http://192.168.0.104:3000/";
const START_ENDPOINT = "what_now/";
const FINISH_ENDPOINT = "logdata/";

void start(Function runCode) async {
  var response = await http.get(BASE_URL + START_ENDPOINT);
  if (response.statusCode == 200){
    print(response.body.toString());
    runCode();
  }else {
    print("Error on starting code");
  }
}

void endCode() async {
  var response = await http.get(BASE_URL + FINISH_ENDPOINT);
  if (response.statusCode != 200){
    print("Error on logging battery status code");
  }
}