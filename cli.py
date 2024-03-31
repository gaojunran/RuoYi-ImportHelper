import json
import re
import shutil
import zipfile
from datetime import datetime
from pathlib import Path

import mysql.connector

cwd = Path.cwd()
DEFAULT_DOWNLOAD = cwd / 'download'
TEMP_PATH = cwd / 'temp'


class WrongConfigException(Exception):
	def __init__(self, message=None):
		self.message = message

	def __str__(self):
		if self.message:
			return "config.json中缺少配置或错误配置：" + self.message
		else:
			return "config.json中缺少配置或错误配置。"


def load_settings() -> dict:
	"""
	载入配置并格式化。
	:return 返回dict类型的配置变量。
	"""
	settings: dict = json.loads(open('config.json').read())

	try:
		# 配置download_path
		if settings['download_path'] == 'default':
			settings['download_path'] = DEFAULT_DOWNLOAD
		else:
			settings['download_path'] = Path(settings['download_path'])
		# 配置ruoyi_path_main
		if settings['ruoyi_path_main'] == 'NotImplemented':
			raise WrongConfigException("ruoyi_path_main")
		else:
			settings['ruoyi_path_main'] = Path(settings['ruoyi_path_main'])
		# 配置ruoyi_path_vue
		if settings['ruoyi_path_vue'] == 'NotImplemented':
			raise WrongConfigException("ruoyi_path_vue")
		else:
			settings['ruoyi_path_vue'] = Path(settings['ruoyi_path_vue'])

		# 配置mysql
		if not settings["mysql"]["if_execute_sql"]:
			settings['mysql'] = None
		else:
			settings['mysql'] = mysql.connector.connect(
				host=settings['mysql']['host'],
				port=settings['mysql']['port'],
				user=settings['mysql']['user'],
				password=settings['mysql']['password'],
				database=settings['mysql']['database'],
			)
	except KeyError:
		raise WrongConfigException()
	return settings


def get_zip() -> Path:
	"""
	获取最新下载的若依代码压缩包。
	如果你要导入的不是当前文件夹里最新的压缩包，你可以重命名这个文件，以让它成为最新的。
	:return: 返回这个文件。
	"""
	files = [file for file in settings['download_path'].glob('*.zip')]
	zip_ = max(files, key=lambda file: file.stat().st_mtime)
	if zip_:
		print(f"你将操作的文件是`\033[34m{zip_}\033[0m`，请确认无误后连按两次回车！")
		input()
		input()
	else:
		raise Exception("你配置的文件夹中没有压缩包！")
	return zip_


def unzip(zip_: Path) -> Path:
	"""
	解压缩，并放置到temp文件夹中。
	:param zip_: 压缩包路径。
	:return: temp中的文件夹路径。
	"""
	with zipfile.ZipFile(zip_) as zipbox:
		dir_name = datetime.now().strftime("%Y%m%d%H%M%S")
		zipbox.extractall(target := TEMP_PATH / dir_name)
	print(f"已经成功解压缩至\033[34m{target}\033[0m。")
	return target


def execute_sql(source: Path):
	"""
	执行SQL。
	:param source: temp中的文件夹路径。
	"""
	cnx: mysql.connector.MySQLConnection = settings['mysql']
	cursor = cnx.cursor()
	for sqlfile in source.glob('*.sql'):
		print(f"你将执行`\033[34m{sqlfile}\033[0m`这个SQL文件；")
		input("请回车确认...")
		with open(sqlfile, encoding='utf-8') as sql_file:
			sql_statements = re.split(r'\n\s*\n', sql_file.read())
			for statement in sql_statements:
				# 忽略注释块
				if statement.strip().startswith("--"):
					continue
				try:
					# 执行SQL语句
					cursor.execute(statement)
					cnx.commit()
				except Exception as e:
					print(f"Error executing SQL statement: {statement}")
					print(f"Error message: {str(e)}")
					cnx.rollback()
	cnx.commit()
	cursor.close()
	cnx.close()
	print("`\033[34m{sqlfile}\033[0m`执行完毕。")


def _generate_tree_preview(path: Path, indent='', show_file=True):
	if show_file and path.is_file():
		print(f"{indent}├── {path.name}")
	elif path.is_dir():
		print(f"{indent}└── {path.name}/")
		for child in path.iterdir():
			_generate_tree_preview(child, indent + "    ", show_file=show_file)


def check_and_exec_copy(source_path: Path, ruoyi_path_main: Path, ruoyi_path_vue: Path):
	"""
	备份并复制文件。
	:param ruoyi_path_main: 来自配置项"ruoyi_path_main"
	:param ruoyi_path_vue:
	:param source_path: 源文件路径。
	"""
	source_main = source_path / "main"
	source_vue = source_path / "vue"

	print(f"压缩包中的`main`文件夹将被复制到：\033[34m{ruoyi_path_main}\033[0m；")
	print(f"新生成代码的目录结构：")
	_generate_tree_preview(source_main)
	print(f"项目目录对应的目录结构：")
	_generate_tree_preview(ruoyi_path_main, show_file=False)
	input("请回车确认...")
	_recursive_copy(source_main, ruoyi_path_main)

	print(f"压缩包中的`vue`文件夹将被复制到：\033[34m{ruoyi_path_vue}\033[0m；")
	print(f"新生成代码的目录结构：")
	_generate_tree_preview(source_vue)
	print(f"项目目录对应的目录结构：")
	_generate_tree_preview(ruoyi_path_vue, show_file=False)
	input("请回车确认...")
	_recursive_copy(source_vue, ruoyi_path_vue)


def _recursive_copy(source_path, target_path):
	for item in source_path.iterdir():
		item_target_path = target_path / item.name

		# 如果目标文件夹中已经存在同名文件或文件夹，备份并替换源文件
		if item_target_path.exists():
			if item.is_file():
				backup_path = target_path / f"_{item.name}.bak"
				shutil.move(item_target_path, backup_path)

		# 复制文件或文件夹到目标文件夹
		if item.is_file():
			shutil.copy2(item, target_path)
		elif item.is_dir():
			new_target_path = target_path / item.name
			new_target_path.mkdir(exist_ok=True)
			_recursive_copy(item, new_target_path)


if __name__ == '__main__':
	settings = load_settings()
	zip_ = get_zip()
	unzip_target = unzip(zip_)
	execute_sql(unzip_target)
	check_and_exec_copy(unzip_target, settings['ruoyi_path_main'], settings['ruoyi_path_vue'])
	print("全部操作已完成！")
