# RuoYi-ImportHelper 若依导入助手

## 简介
一个简易的Python脚本，将若依生成的代码压缩包预览并直接放入各个目录中，并自动执行SQL代码。

## 使用简介
1. 下载项目到本地，在本项目根目录运行`pip install -r requirements.txt`，安装依赖。
2. 配置`settings.json`：
   - `download_path`：从官网下载若依代码压缩包到本地的位置，可以使用绝对路径。如果使用相对路径，则是相对本项目根目录而言；默认值为default，下载到根目录下的`download`文件夹中。
   - `ruoyi_path_main`：若依项目后端代码路径，路径末尾应为`main`。
   - `ruoyi_path_vue`：若依项目前端代码路径，路径末尾应为`src`。
   - `mysql`：配置MySQL环境，以自动执行SQL语句。其中`if_execute_sql`字段为`false`，表示不执行SQL语句。
3. 在`download_path`对应的路径有若依代码压缩包的情况下，使用Python解释器运行`cli.py`。
