Dim $aArray[6]

$aArray[0]="testRT01"
$aArray[1]="testRT02"
$aArray[2]="testRT03"
$aArray[3]="testRT04"
$aArray[4]="testRT05"
$aArray[5]="testRT06"

FOR $element IN $aArray
	RunWait('java -cp "./;../;../ParameterLoader;../lib/poi-4.0.1/*" Jikkou01_NewStandardWork '&$element&" input/�V�e�X�g�\�[�X.xls �e�X�g�p�����[�^ �^�[�Q�b�g ���s01")
NEXT