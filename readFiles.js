const { match } = require("assert");
const fs = require("fs");
const { platform } = require("os");
const path = require("path")

const sep = path.sep
const experiment_folder = process.cwd() + sep + "experiment-results"

const benchmarkData = getBenchmarkData(fs, sep, experiment_folder)
console.log(benchmark_to_csv(benchmarkData));


function benchmark_to_csv(benchmark) {
    let csv_text = ""
    benchmark.forEach(item => {
        csv_text += item.platform + ",,\r\n"
        item.results.forEach(element => { 
            csv_text += element.program + ",,\r\n"
            csv_text += "mAh, foreground_time, cpu_time" + "\r\n"
            element.results.forEach(execution => {
                csv_text += `${execution.mAh} ,${execution.foreground_time},${execution.cpu_time}\r\n`
            })
        })
    });
    return csv_text
}


function getBenchmarkData(fs, sep, folder) {   
    return fs.readdirSync(folder).map(platform => {
        const platform_folder = folder + sep + platform
        const programs = fs.readdirSync(platform_folder).map(device => {
            const device_folder = platform_folder + sep + device
            return fs.readdirSync(device_folder).map(program => {
                const program_folder = device_folder + sep + program
                const files = fs.readdirSync(program_folder)
                const orderedFiles = orderFileNames(files)
                const benchmarks = orderedFiles.map(file => {
                    const file_text = fs.readFileSync(program_folder + sep + file, {encoding: "utf-8"})
                    const program_id = file_text.match(/top=(.*):/)[1]
                    return get_data(file_text)
                })
                return {
                    program: program,
                    results: benchmarks
                }
            })
        })
        return {
            platform: platform,
            results: programs[0]
        }
    });
}


function get_data(file_text) {
    const program_id = file_text.match(/top=(.*):/)[1]
    const mAh_regex = RegExp(`Uid ${program_id}: (.*) \\(`)
    const foreground_time_regex = RegExp(`\\n\\s*${program_id}:[\\s\\S]*?Foreground for: (.*) `)
    const cpu_time_regex = RegExp(`\\n\\s*${program_id}:[\\s\\S]*?Total cpu time: u=(.*) s`)
    return {
        mAh: findText(mAh_regex, file_text),
        foreground_time: findText(foreground_time_regex, file_text),
        cpu_time: findText(cpu_time_regex, file_text)
    }
}

function findText(regex, string) {
    const regex_return = string.match(regex)
    if(regex_return != null){
        return regex_return[1]
    }
    return "null"
}

function orderFileNames(files) {
    const nameTemplate = getNameTemplate(files[0])
    const intList = files.map((file) => {
        return getFileNumber(file)
    })
    const orderedFiles = intList.sort((a,b) => {
        return a - b
    })
    return orderedFiles.map((fileInt) => {
        return nameTemplate[0] + fileInt + nameTemplate[1]
    })
}

function getNameTemplate(string) {
    const numberPosition = getPosition(string, "-", 3) + 1
    const prefix = string.substring(0, numberPosition)
    const sufix = string.substring(string.length - 4, string.length)
    return [prefix, sufix]
}

function getFileNumber(string) {
    const dashSplit = string.split("-")
    const lastElement = dashSplit[dashSplit.length - 1]
    return parseInt(lastElement.substring(0,lastElement.length-4))
}

function getPosition(string, subString, index) {
    return string.split(subString, index).join(subString).length
}
