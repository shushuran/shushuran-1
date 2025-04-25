// 动态规划算法示例集合

// 斐波那契数列
function fibonacci(n) {
    n = parseInt(n);
    if (isNaN(n) || n < 0) {
        throw new Error('请输入有效的正整数！');
    }
    if (n <= 1) return [0, 1].slice(0, n + 1);
    const dp = new Array(n + 1);
    dp[0] = 0;
    dp[1] = 1;
    for (let i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp;
}

// 0-1背包问题
function knapsack(weights, values, capacity) {
    if (!Array.isArray(weights) || !Array.isArray(values) || weights.length !== values.length) {
        throw new Error('权重和价值的数量必须相同！');
    }
    if (isNaN(capacity) || capacity < 0) {
        throw new Error('请输入有效的容量值！');
    }
    const n = weights.length;
    const dp = Array.from({length: n + 1}, () => Array(capacity + 1).fill(0));
    for (let i = 1; i <= n; i++) {
        for (let j = 0; j <= capacity; j++) {
            if (weights[i - 1] <= j) {
                dp[i][j] = Math.max(
                    dp[i - 1][j],
                    dp[i - 1][j - weights[i - 1]] + values[i - 1]
                );
            } else {
                dp[i][j] = dp[i - 1][j];
            }
        }
    }
    return dp;
}

// 最长公共子序列
function lcs(text1, text2) {
    const m = text1.length, n = text2.length;
    const dp = Array.from({length: m + 1}, () => Array(n + 1).fill(0));

    for (let i = 1; i <= m; i++) {
        for (let j = 1; j <= n; j++) {
            if (text1[i - 1] === text2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }

    return dp;
}

// DOM元素
// 问题选择器
const problemSelector = document.getElementById('problem-selector');
const fibonacciInput = document.getElementById('fibonacci-input');
const knapsackWeights = document.getElementById('knapsack-weights');
const knapsackValues = document.getElementById('knapsack-values');
const knapsackCapacity = document.getElementById('knapsack-capacity');
const lcsText1 = document.getElementById('lcs-text1');
const lcsText2 = document.getElementById('lcs-text2');

// 根据选择的问题显示对应的输入框
problemSelector.addEventListener('change', () => {
    document.querySelectorAll('.input-group input').forEach(input => {
        input.classList.add('hidden');
    });
    switch(problemSelector.value) {
        case 'fibonacci':
            fibonacciInput.classList.remove('hidden');
            break;
        case 'knapsack':
            knapsackWeights.classList.remove('hidden');
            knapsackValues.classList.remove('hidden');
            knapsackCapacity.classList.remove('hidden');
            break;
        case 'lcs':
            lcsText1.classList.remove('hidden');
            lcsText2.classList.remove('hidden');
            break;
    }
});
const problemInput = document.getElementById('problem-input');
const solveBtn = document.getElementById('solve-btn');
const stepsOutput = document.getElementById('steps-output');

// 事件监听
// 处理不同问题的求解
const problemHandlers = {
    'fibonacci': (inputValue) => {
        const dpTable = fibonacci(inputValue);
        return `<h3>DP表：</h3>
            <table class="dp-table">
                ${dpTable.map((row, i) => `<tr>
                    <th>${i}</th>
                    <td style="background-color: ${i % 2 === 0 ? '#f0f8ff' : '#e6f7ff'}">${row}</td>
                </tr>`).join('')}
            </table>
            <p class="result">最终结果：<span>${dpTable[inputValue]}</span></p>`;
    },
    'knapsack': (inputValue) => {
        const [weightsStr, valuesStr, capacityStr] = inputValue.split(';');
const weights = weightsStr.split(',').map(Number);
const values = valuesStr.split(',').map(Number);
const capacity = Number(capacityStr);
if (weights.some(isNaN) || values.some(isNaN) || isNaN(capacity)) {
    throw new Error('请输入有效的数字！');
}
        const dpTable = knapsack(weights, values, capacity);
        return `<h3>DP表：</h3>
            <table class="dp-table">
                ${dpTable.map((row, i) => `<tr>
                    <th>${i}</th>
                    <td>${row.map((cell, j) => `<span style="background-color: ${cell === dpTable[weights.length][capacity] ? '#e6ffe6' : '#f0fff0'}">${cell}</span>`).join('')}</td>
                </tr>`).join('')}
            </table>
            <p class="result">最大价值：<span>${dpTable[weights.length][capacity]}</span></p>`;
    },
    'lcs': (inputValue) => {
        const [text1, text2] = inputValue.split(';');
        const dpTable = lcs(text1, text2);
        return `<h3>DP表：</h3>
            <table class="dp-table">
                ${dpTable.map((row, i) => `<tr>
                    <th>${i}</th>
                    <td>${row.map((cell, j) => `<span style="background-color: ${cell === dpTable[text1.length][text2.length] ? '#e6ffe6' : '#f0fff0'}">${cell}</span>`).join('')}</td>
                </tr>`).join('')}
            </table>
            <p class="result">最长公共子序列长度：<span>${dpTable[text1.length][text2.length]}</span></p>`;
    }
};

solveBtn.addEventListener('click', () => {
    const selectedProblem = problemSelector.value;
let inputValue;
switch(selectedProblem) {
    case 'fibonacci':
        inputValue = fibonacciInput.value;
        break;
    case 'knapsack':
        inputValue = `${knapsackWeights.value};${knapsackValues.value};${knapsackCapacity.value}`;
        break;
    case 'lcs':
        inputValue = `${lcsText1.value};${lcsText2.value}`;
        break;
}
    if (!problemHandlers[selectedProblem]) {
    alert('请选择有效的问题类型！');
    return;
}

if (selectedProblem === 'fibonacci') {
        if (isNaN(inputValue) || inputValue < 0) {
            alert('请输入有效的正整数！');
            return;
        }
    } else if (selectedProblem === 'knapsack') {
        if (!knapsackWeights.value || !knapsackValues.value || !knapsackCapacity.value) {
            alert('请填写完整的背包问题参数！');
            return;
        }
    } else if (selectedProblem === 'lcs') {
        if (!lcsText1.value || !lcsText2.value) {
            alert('请填写两个字符串！');
            return;
        }
    }

    // 执行算法
    const resultHTML = problemHandlers[selectedProblem](inputValue);

    // 显示结果
    stepsOutput.innerHTML = resultHTML;

    // 添加详细步骤解释
    const explanation = {
        'fibonacci': `斐波那契数列计算步骤：
1. 初始化DP表
2. 填充DP表
3. 返回结果`, 
        'knapsack': `0-1背包问题计算步骤：
1. 初始化二维DP表
2. 遍历物品和容量
3. 根据选择更新DP表
4. 返回最大价值`, 
        'lcs': `最长公共子序列计算步骤：
1. 初始化二维DP表
2. 遍历两个字符串
3. 根据字符匹配更新DP表
4. 返回最长公共子序列长度`
    };
    stepsOutput.insertAdjacentHTML('beforeend', `<div class="steps-explanation">${explanation[selectedProblem]}</div>`);

    // 添加动画效果
    stepsOutput.style.opacity = 0;
    setTimeout(() => {
        stepsOutput.style.transition = 'opacity 0.5s ease';
        stepsOutput.style.opacity = 1;
    }, 100);
    
});